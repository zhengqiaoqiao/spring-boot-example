package org.example.server.ftp;

import java.io.File;   
import java.io.FileInputStream;
import java.io.FileOutputStream;   
import java.io.IOException;   
import java.io.InputStream;   
import java.io.OutputStream;   
import java.io.PrintWriter;    
  

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.PrintCommandListener;   
import org.apache.commons.net.ftp.FTP;   
import org.apache.commons.net.ftp.FTPClient;   
import org.apache.commons.net.ftp.FTPFile;   
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;   
import org.example.common.convert.ConvertUtil;
import org.example.common.encryp.MD5Util;
import org.example.common.file.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

  
/**
 * FTP工具类
 * <p>Title: FTPUtil</p>
 * <p>Description: 上传下载（支持断点）以及文件合并等</p>
 * @author: zheng.qq
 * @date: 2016年11月25日
 */
public class FTPUtil {   
	private final static Logger logger = LoggerFactory.getLogger(FTPUtil.class);
    public FTPClient ftpClient = new FTPClient(); 
    private String osName = "linux";
    private String separator = "/";  
       
    public FTPUtil(){   
        //设置将过程中使用到的命令输出到控制台   
        this.ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
    }   
       
    /**
     * 连接到FTP服务器  
     * @param hostname 主机名  
     * @param port 端口  
     * @param username 用户名  
     * @param password 密码  
     * @return 是否连接成功  
     * @throws IOException  
     */  
    public boolean connect(String hostname,int port,String username,String password) throws IOException{   
        ftpClient.connect(hostname, port);   
        ftpClient.setControlEncoding("UTF-8");      
        if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){   
            if(ftpClient.login(username, password)){  
            	String systemType = ftpClient.getSystemType();
                if(systemType.toLowerCase().startsWith("win")){
                	this.osName = "windows";
                	this.separator = "\\";
                }else{
                	this.osName = "linux";
                	this.separator = "/";
                }
            	//设置PassiveMode传输   
                ftpClient.enterLocalPassiveMode();   
                //设置以二进制流的方式传输   
                try {
        			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			logger.error("设置ftp传输方式异常：{}", e);
        		}   
                return true;   
            }   
        }   
        disconnect();   
        return false;   
    }
    /**
     * 断开与远程服务器的连接  
     * @throws IOException  
     */  
    public void disconnect() throws IOException{   
        if(ftpClient.isConnected()){   
            ftpClient.disconnect();   
        }   
    }   
       
    /**
     * 从FTP服务器上下载文件,支持断点续传，下载百分比汇报  
     * @param remote 远程文件路径  
     * @param local 本地文件路径  
     * @return 下载的状态  
     * @throws IOException  
     */  
    public boolean download(String remote, String local) throws IOException{    
    	remote = FileUtil.getRealFilePath(remote, osName);
    	local = FileUtil.getRealFilePath(local, null);
        //检查远程文件是否存在   
        FTPFile[] files = ftpClient.listFiles(remote);   
        if(files.length != 1){   
            logger.error("远程文件不存在");
            return false;   
        } 
        File localFile = new File(local);
        if(localFile.exists()){
        	logger.error("本地文件已存在");   
            return false;   
        }
        FTPFile ftpFile = files[0];
        long remoteSize = ftpFile.getSize(); 
        //显示进度的上传   
        long step = remoteSize/100;   
        long process = 0L;   
        long localTempSize = 0L;
        
        String mdStr = null;
        try {
			mdStr = MD5Util.getMD5String(ftpFile.getLink() + ftpFile.getSize() + ftpFile.getTimestamp().toString());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.error("获取远程服务器文件的MD5异常：{}！", e);
        	return false;
		}
        if(mdStr==null){
        	logger.error("获取远程服务器文件的MD5失败！");
        	return false;
        }
        //获取本地文件目录
        String directory = local.substring(0, local.lastIndexOf(File.separatorChar)+1);   
        String tempFilePath = directory + File.separatorChar + mdStr + ".tmp";
        File tempFile = new File(tempFilePath); 
       
        if(tempFile.exists()){   
        	localTempSize = tempFile.length();
        	process= localTempSize/step;
        }
        
        //判断本地文件大小是否大于远程文件大小   
        if(localTempSize >= remoteSize){   
        	logger.error("本地文件大于远程文件，下载中止");   
            return false;   
        }
        //进行断点续传，并记录状态   
        FileOutputStream out = new FileOutputStream(tempFile, true);   
        ftpClient.setRestartOffset(localTempSize);
        InputStream in = ftpClient.retrieveFileStream(new String(remote.getBytes("GBK"),"iso-8859-1"));   
        byte[] bytes = new byte[1024]; 
        int c;   
        while((c = in.read(bytes))!= -1){   
            out.write(bytes,0,c);   
            localTempSize+=c;   
            long nowProcess = localTempSize/step;   
            if(nowProcess > process){   
                process = nowProcess;
                logger.debug("下载进度：" + process + "%");
            }   
        }   
        in.close();   
        out.close();   
        boolean isDo = ftpClient.completePendingCommand();   
        if(isDo){
        	File oldFile = new File(tempFilePath);
    		if(!oldFile.renameTo(localFile)){
    			logger.error("重命名本地文件失败！");   
                return false;   
    		}
        } else {   
        	logger.error("重命名本地文件失败！");   
            return false;   
        }
        return true;   
    }
       

    /**
     * 上传文件到FTP服务器，支持断点续传  
     * @param in 本地文件名称，绝对路径  
     * @param remote 远程文件路径
     * @return 上传结果  
     * @throws IOException  
     */  
    public boolean upload(String filePath, InputStream in) throws IOException{ 
    	filePath = FileUtil.getRealFilePath(filePath, osName);
    	long originSize = in.available();
    	//显示进度的上传   
        long step = originSize / 100;   
        long process = 0;   
        long localreadbytes = 0L;
        long fileSize = 0L;
    	//检查远程文件是否存在   
        FTPFile[] files = ftpClient.listFiles(new String(filePath.getBytes("GBK"),"iso-8859-1"));   
        if(files.length > 0){   
            logger.error("远程文件已存在");
            return false;   
        } 
        String md5 = "";
		try {
			md5 = MD5Util.getMD5String(filePath);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String fileDir = "";
        if(filePath.contains(this.separator)){   
        	fileDir = filePath.substring(0, filePath.lastIndexOf(this.separator));  
        }
        //创建服务器远程目录结构，创建失败直接返回   
        if(!createDir(fileDir)){
        	logger.error("创建上传目录失败！");
        	return false;
        }
        String tempFilePath = fileDir + this.separator + md5 + ".tmp";
        //检查远程是否存在文件   
        FTPFile[] tmpFiles = ftpClient.listFiles(tempFilePath);   
        if(tmpFiles.length == 1){   
            fileSize = tmpFiles[0].getSize();   
            if(fileSize==originSize){
            	if(ftpClient.rename(tempFilePath, filePath)){
            		logger.error("修改文件名称失败！");
                	return false;
            	}
            } else if(fileSize>0){
            	//尝试移动文件内读取指针,实现断点续传
                ftpClient.setRestartOffset(fileSize);   
                process = fileSize /step;   
                in.skip(fileSize);
                localreadbytes = fileSize;   
                
            }   
        }
        OutputStream out = ftpClient.appendFileStream(tempFilePath);   
        byte[] bytes = new byte[1024];   
        int c;   
        while((c = in.read(bytes))!= -1){   
            out.write(bytes,0,c);   
            localreadbytes+=c;   
            if(localreadbytes / step != process){   
                process = localreadbytes / step;   
                System.out.println("上传进度:" + process + "%");   
                //TODO 汇报上传状态   
            }   
        }   
        out.flush();   
        in.close();   
        out.close();  
        boolean isDo = ftpClient.completePendingCommand(); 
        if(isDo){
        	//修改文件名称
        	if(!ftpClient.rename(tempFilePath, filePath)){
        		logger.error("修改文件名称失败！");
            	return false;
        	}
        }else{
        	logger.error("上传文件失败！");
        	return false;
        }
        return true;

    }
    
    
    
    /**
     * 读文件
     * @param ftpClient
     * @param filePath
     * @return
     * @throws IOException 
     */
    public byte[] readFileByte(String filePath){
    	filePath = FileUtil.getRealFilePath(filePath, osName);
    	byte[] bytes = null;
        FTPFile[] files = null;
        InputStream in = null;
		try {
			files = ftpClient.listFiles(filePath);
			if(files==null || files.length < 1){
				logger.error("FTP服务器不存在该文件:{}！", filePath);
			}else{
				in = ftpClient.retrieveFileStream(filePath);
				if (in != null) {
					bytes = IOUtils.toByteArray(in);
					IOUtils.closeQuietly(in);
					ftpClient.completePendingCommand();
		        }
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("读取FTP文件失败！异常原因:{}！", e);
		} 
        return bytes;
    }
    
    
    /**
     * 写文件
     * @param filePath
     * @param bytes
     * @param rewrite
     * @return
     */
    public boolean writeFile(String filePath, byte[] bytes, boolean rewrite){
    	filePath = FileUtil.getRealFilePath(filePath, osName);
    	boolean rs = false;
    	FTPFile[] files = null;
    	OutputStream out = null;
    	InputStream in = null;
		try {
			in = ConvertUtil.byteToInputStream(bytes);
			if(in==null){
				return false;
			}
			files = ftpClient.listFiles(filePath);
			if(files!=null && files.length == 1 && !rewrite){
				//续传
				FTPFile file = files[0];
				long offset = file.getSize();
				ftpClient.setRestartOffset(offset);
			}
			out = ftpClient.appendFileStream(filePath);
			if(out != null){
				IOUtils.copyLarge(in, out);
				IOUtils.closeQuietly(out);
				ftpClient.completePendingCommand();
				rs = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("程序发生异常:{}！", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("程序发生异常:{}！", e);
		} finally{
			if(in != null){
				IOUtils.closeQuietly(in);
			}
		}
		return rs;
    }
    
    /**
     * 
     * @param out
     * @param in
     * @param offset
     * @return
     */
    public boolean writeFile(OutputStream out, InputStream in, long offset){
    	boolean rs = true;
    	ftpClient.setRestartOffset(offset);
		try {
			IOUtils.copyLarge(in, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("程序发生异常:{}！", e);
			rs = false;
		}
		return rs;
    }
    
    /**
     * 合并文件
     * @param filePathList
     * @param filePath
     * @return
     */
    public boolean mergeFile(List<String> filePathList, String filePath){
    	filePath = FileUtil.getRealFilePath(filePath, osName);
    	boolean isDo = true;
    	for(String filePathItem : filePathList){
    		filePathItem = FileUtil.getRealFilePath(filePathItem, osName);
    		byte[] bytes = readFileByte(filePathItem);
    		if(bytes != null){
    			if(writeFile(filePath, bytes, false)){
    				try {
						ftpClient.deleteFile(filePathItem);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.error("程序发生异常：{}", e);
					}
        			continue;
        		}else{
        			isDo = false;
        		}
        		if(!isDo){
        			break;
        		}
    		}	
    	}
		return true;
    }
    
    public boolean deleteDir(String dir){
    	dir = FileUtil.getRealFilePath(dir, osName);
    	boolean rs = true;
    	FTPFile[] files = null;
    	try {
    		files = ftpClient.listFiles(dir);
    		if(files==null || files.length<1){
    			rs = ftpClient.removeDirectory(dir);
    		}else{
    			boolean isDo = true;
    			for(FTPFile file : files){
    				String filepath = dir + this.separator + file.getName();
    				if(ftpClient.deleteFile(filepath)){
    					continue;
    				}else{
    					isDo = false;
    					break;
    				}
    			}
    			if(isDo){
    				rs = ftpClient.removeDirectory(dir);
    			}else{
    				rs = false;
    			}
    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return rs;
    }
    /**
     * 创建目录  （支持多级目录创建）
     * @param ftpClient
     * @param dir
     * @return
     */
    public boolean createDir(String dir){
    	dir = FileUtil.getRealFilePath(dir, osName);
    	//从根目录开始验证
    	boolean rs = true;
    	String pwd = "";
    	try {
    		pwd = ftpClient.printWorkingDirectory();
    		//目录不存在
			if(!ftpClient.changeWorkingDirectory(dir)){
				String temp = "";
				String[] itemList = null;
				if(this.osName.equals("windows")){
					itemList = dir.split("\\"+this.separator);
				}else{
					itemList = dir.split(this.separator);
				}
				if(itemList!=null){
					for(String item : itemList){
						if(!StringUtils.isEmpty(item)){
							temp = item + this.separator;
							if(!ftpClient.changeWorkingDirectory(temp)){
								if(!ftpClient.makeDirectory(temp)){
									rs = false;
									break;
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("程序发生异常：{}", e);
			rs = false;
		} finally{
			if(pwd!=null && !pwd.equals("")){
				try {
					ftpClient.changeWorkingDirectory(pwd);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return rs;
    }
    
    /**
     * 获取FTP目录中文件
     * @param dir
     * @return
     */
    public List<FTPFile> listFile(String dir){
    	dir = FileUtil.getRealFilePath(dir, osName);
    	List<FTPFile> fileList = null;
    	try {
    		FTPFile[] files = ftpClient.listFiles(dir);
    		fileList = Arrays.asList(files);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return fileList;
    }
    
    /**
     * 判断ftp文件是否存在
     * @param filePath
     * @return
     */
    public boolean isExistFile(String filePath){
    	filePath = FileUtil.getRealFilePath(filePath, osName);
    	boolean rs = true;
    	try {
    		FTPFile[] files = ftpClient.listFiles(filePath);
    		if(files!=null && files.length==1){
    			rs = true;
    		}else{
    			rs = false;
    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rs = false;
		}
    	return rs;
    }
    
       
    public static void main(String[] args) {
        FTPUtil myFtp = new FTPUtil();   
        try {   
        	myFtp.connect("192.168.1.102", 21, "QIAO-OS", "root@1234"); 
//        	myFtp.connect("10.0.20.107", 21, "zheng.qq", "qiao@1234"); 
            File file = new File("F:\\redis-cli.exe");
            FileInputStream in = new FileInputStream(file);
            System.out.println(myFtp.upload("soft/redis-cli.exe", in));  
//            System.out.println(myFtp.download("/ 央视走西口/新浪网/走西口24.mp4", "E:\\走西口242.mp4"));   
//            System.out.println(myFtp.createDir("soft/s/gg"));
            myFtp.disconnect();   
        } catch (IOException e) {   
            System.out.println("连接FTP出错："+e.getMessage());   
        }   
    }   
}  
