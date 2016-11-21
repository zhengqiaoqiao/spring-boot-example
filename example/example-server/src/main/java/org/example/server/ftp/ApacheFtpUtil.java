package org.example.server.ftp;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class ApacheFtpUtil {
	private FTPClient ftpClient;
	private String strIp;
	private int intPort;
	private String user;
	private String password;
	
	private static Logger logger = Logger.getLogger(ApacheFtpUtil.class.getName());

	/* *
	 * Ftp构造函数
	 */
	public ApacheFtpUtil(String strIp, int intPort, String user, String Password) {
		this.strIp = strIp;
		this.intPort = intPort;
		this.user = user;
		this.password = Password;
		this.ftpClient = new FTPClient();
	}
	
	/**
	 * @return 判断是否登入成功
	 * */
	public boolean connectServer() {
		boolean isLogin = false;
		FTPClientConfig ftpClientConfig = new FTPClientConfig();
		ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
		this.ftpClient.setControlEncoding("GBK");
		this.ftpClient.configure(ftpClientConfig);
		try {
			if (this.intPort > 0) {
				this.ftpClient.connect(this.strIp, this.intPort);
			} else {
				this.ftpClient.connect(this.strIp);
			}
			// FTP服务器连接回答
			int reply = this.ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				this.ftpClient.disconnect();
				throw new IOException("Can't connect to server '" + this.strIp + "'");
			}
			
			isLogin = this.ftpClient.login(this.user, this.password);
			if(!isLogin){
				closeServer();
	            throw new IOException("Can't login to server '" + this.strIp + "'");
			}
			
			// 设置传输协议
			this.ftpClient.enterLocalPassiveMode();
			this.ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			logger.info("恭喜" + this.user + "成功登陆FTP服务器:"+ftpClient.printWorkingDirectory());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(this.user + "登录FTP服务失败！" + e.getMessage());
			closeServer();
		}
		this.ftpClient.setBufferSize(1024 * 8);
		this.ftpClient.setConnectTimeout(30 * 1000);
		this.ftpClient.setDataTimeout(60 * 1000);
		return isLogin;
	}

	/**
	 * @退出关闭服务器链接
	 * */
	public void closeServer() {
		if (null != this.ftpClient && this.ftpClient.isConnected()) {
			try {
				boolean reuslt = this.ftpClient.logout();// 退出FTP服务器
				if (reuslt) {
					logger.info("成功退出服务器");
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.warn("退出FTP服务器异常！" + e.getMessage());
			} finally {
				try {
					this.ftpClient.disconnect();// 关闭FTP服务器的连接
				} catch (IOException e) {
					e.printStackTrace();
					logger.warn("关闭FTP服务器的连接异常！");
				}
			}
		}
	}
	
	/**
	 * 取得相对于当前连接目录的某个目录下所有文件列表
	 * Lists the files in the given FTP directory.
	 * @param path
	 * @return
	 */
	public List<String> getFileList(String path) {
		ftpClient.enterLocalPassiveMode();
		List<String> list = new ArrayList<String>();
		try {
			FTPFile[] ftplist = ftpClient.listFiles(path);
	        int size = (ftplist == null) ? 0 : ftplist.length;
	        for (int i = 0; i < size; i++) {
	        	 FTPFile ftpFile = ftplist[i];
	             if (ftpFile.isFile()) {
	            	 list.add(ftpFile.getName());
	             }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	public FTPFile[] getDataFileList(String path,final String pattern) throws IOException{
		ftpClient.enterLocalPassiveMode();
		FTPFile[] list = ftpClient.listFiles(path, new FTPFileFilter() { 
			@Override
			public boolean accept(FTPFile file) { 
				Pattern p = Pattern.compile(pattern);
				if (file.isFile() && p.matcher(file.getName()).find()) return true ;
				return false ;
			}}) ;

		return list;
	}

	/**
	 * 获取ftp远程文件大小
	 * @param filename
	 * @return
	 */
	public long getFileSize(String filename) {
		long fileSize = -1;
		filename = filename.replaceAll("\\\\", "/");
		try {
			FTPFile[] list = ftpClient.listFiles(filename);
			if(list!=null&&list.length>0){
				return list[0].getSize();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileSize;
	}
   

	/***
	 * 上传Ftp文件重载方法
	 * @param localFile 当地文件路径
	 * @param romotUpLoadePath上传服务器绝对路径,路径以 "/"分开 
	 * */
	public boolean uploadFile(String localFilepath, String romotUpLoadePath) {
		
		File localFile = new File(localFilepath);
		BufferedInputStream inStream = null;
		boolean success = false;
		try {
			String romoteparent = romotUpLoadePath.substring(0,romotUpLoadePath.lastIndexOf("/"));
			createDir(romoteparent);
			inStream = new BufferedInputStream(new FileInputStream(localFile));
			logger.info(localFile.getName() + "开始上传.....");
			success = this.ftpClient.storeFile(romotUpLoadePath, inStream);
			if (success == true) {
				logger.info(localFile.getName() + "上传成功");
				return success;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(localFile + "未找到");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	
	public static String formatSize(Long fileSize) {
		String formatSize = "";
		DecimalFormat df = new DecimalFormat("0.##");
		if (fileSize / (1024 * 1024 * 1024) > 0) {
			formatSize = df.format(Float.parseFloat(fileSize.toString())/ (1024 * 1024 * 1024))+ " GB";
		} else if (fileSize / (1024 * 1024) > 0) {
			formatSize = df.format(Float.parseFloat(fileSize.toString())/ (1024 * 1024))+ " MB";
		} else if (fileSize / (1024) > 0) {
			formatSize = df.format(Float.parseFloat(fileSize.toString())/ (1024))+ " KB";
		} else {
			formatSize = fileSize + " 字节";
		}
		return formatSize;
	}

	/**
	 * 断点续传FTP资源
	 * @param remote
	 * @param local
	 * @return
	 * @throws IOException
	 */
	public long downloadFTP(String remote, String local,long lRemoteSize)  {

		
		File f = new File(local);
		InputStream is = null;
		OutputStream out = null;
		BufferedInputStream bis = null;
		long bytesum = 0;
		int byteread = 0;
		long fileSize = 0;
		try {
			ftpClient.enterLocalPassiveMode();
			//文件名称
			String fileName = remote.substring(remote.lastIndexOf('/')+1);  
			
			if (f.exists()) {
				fileSize = f.length();
			}
			
			out = new FileOutputStream(f, true);
			
			String progressInfo = "("+fileName+","+formatSize(fileSize+bytesum)+"/"+formatSize(lRemoteSize)+")";

			//如果文件已经下载完毕，直接返回
			if (f.length() >= lRemoteSize) {
				System.out.println("ftp download progress：已下载完毕 "+progressInfo);
				out.close();
				return f.length();
			}
			ftpClient.setRestartOffset(fileSize);
			ftpClient.setBufferSize(1204 * 8);
			byte[] buffer = new byte[1204 * 8];
			
			is = ftpClient.retrieveFileStream(remote);//result = ftpClient.retrieveFile(remote, out);
			bis = new BufferedInputStream(is);
		    long step = lRemoteSize /100;   
	        long process= (fileSize+bytesum) /step;
            System.out.println("ftp download progress： "+process +"%"+progressInfo);   

			while ((byteread = bis.read(buffer)) != -1) {
				bytesum += byteread;
				out.write(buffer, 0, byteread);
				long nowProcess = (fileSize+bytesum) /step;   
	            if(nowProcess > process){   
	                 process = nowProcess;   
	                 System.out.println("ftp download progress： "+process +"% ("+fileName+","+formatSize(fileSize+bytesum)+"/"+formatSize(lRemoteSize)+")");   
	            }   
			}
			out.flush();
		}catch(SocketTimeoutException e){
			return -2;
		} catch(Exception e) {
			e.printStackTrace();
			return -1;
		} finally{
			try{
				if(is!=null){
					is.close();
					ftpClient.completePendingCommand();//is的close()后面调用，避免程序死掉
				}
				if(bis!=null){
					bis.close();
				} 
				if(out!=null){
					out.close();
				} 
			}catch(IOException ie){
				ie.printStackTrace();
			}
		}
		
		return f.length();
	}
	
	/***
	 * @上传文件夹
	 * @param localDirectory
	 *            当地文件夹
	 * @param remoteDirectoryPath
	 *            Ftp 服务器路径 以"/"分隔 (FTP上的文件夹)
	 * */
	public boolean uploadDirectory(String localDirectory, String remoteDirectoryPath) {
		File src = new File(localDirectory);
		createDir(remoteDirectoryPath);
		File[] allFile = src.listFiles();
		for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
			if (!allFile[currentFile].isDirectory()) {
				String srcName = allFile[currentFile].getPath().toString();
				uploadFile(srcName, remoteDirectoryPath);
			}
		}
		for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
			if (allFile[currentFile].isDirectory()) {
				// 递归
				String remoteDirPath =remoteDirectoryPath +"/"+allFile[currentFile].getName();
				uploadDirectory(allFile[currentFile].getPath().toString(),remoteDirPath);
			}
		}
		return true;
	}
	/**
	 * 在当前目录创建目录
	 */
	private boolean createDir(String remoteDirectoryPath) {
		
		try {
			if(!this.ftpClient.changeWorkingDirectory(remoteDirectoryPath)){
				String[] pathdir = remoteDirectoryPath.split("/");
				String tempRemote = "";
				for(int i=1;i<pathdir.length;i++){
					tempRemote += ("/"+pathdir[i]);
					if(!this.ftpClient.changeWorkingDirectory(tempRemote)){
						this.ftpClient.makeDirectory(tempRemote);
					}
				}
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			logger.info(remoteDirectoryPath + "目录创建失败");
		}
		return false;
	}
	/***
	 * @下载文件夹
	 * @param localDirectoryPath本地地址
	 * @param remoteDirectory 远程文件夹
	 * */
	public boolean downLoadDirectory(String localDirectoryPath,String remoteDirectory) {
		try {
			String fileName = new File(remoteDirectory).getName();
			localDirectoryPath = localDirectoryPath + fileName + "//";
			new File(localDirectoryPath).mkdirs();
			FTPFile[] allFile = this.ftpClient.listFiles(remoteDirectory);
			for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
				if (!allFile[currentFile].isDirectory()) {
					downloadFTP(remoteDirectory+allFile[currentFile].getName(),localDirectoryPath+allFile[currentFile].getName(),allFile[currentFile].getSize());
				}
			}
			for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
				if (allFile[currentFile].isDirectory()) {
					String strremoteDirectoryPath = remoteDirectory + "/"+ allFile[currentFile].getName();
					downLoadDirectory(localDirectoryPath,strremoteDirectoryPath);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("下载文件夹失败");
			return false;
		}
		return true;
	}
	// FtpClient的Set 和 Get 函数
	public FTPClient getFtpClient() {
		return ftpClient;
	}
	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}
	
	public static void main(String[] args) throws IOException, Exception {
		ApacheFtpUtil ftp =new ApacheFtpUtil("ladsweb.nascom.nasa.gov",21,"anonymous","anonymous");
		ftp.connectServer();//".*(h25v05|h25v06).*hdf$"
		FTPFile[] fileList=ftp.getDataFileList("allData/6/MOD13Q1/2016/145/",".*hdf$");
		for(int i=0;i<fileList.length;i++){
			FTPFile df = fileList[i];
			System.out.println("开始下载文件："+df.getName());
			ftp.downloadFTP("allData/6/MOD13Q1/2016/145/"+df.getName(), "D:\\temp\\"+df.getName(), df.getSize());
		}

		ftp.closeServer();
	}
}