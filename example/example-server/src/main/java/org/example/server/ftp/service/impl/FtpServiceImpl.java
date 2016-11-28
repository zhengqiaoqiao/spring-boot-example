package org.example.server.ftp.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.example.common.file.FileUtil;
import org.example.server.ftp.FTPUtil;
import org.example.server.ftp.service.IFtpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class FtpServiceImpl implements IFtpService{
	private final Logger logger = Logger.getLogger(FtpServiceImpl.class);
	@Resource
	private MessageChannel ftpUploadChannel;
	@Resource
	private MessageChannel ftpRemoveChannel;
	@Value("${ftp.server.ip}")
	private String ftpServerIp;
	@Value("${ftp.server.port}")
	private int ftpServerPort;
	@Value("${ftp.server.username}")
	private String ftpServerUserName;
	@Value("${ftp.server.password}")
	private String ftpServerPassword;
	
	@Override
	public boolean uploadFile(InputStream in, String ftpServerPath, String fileName) throws Exception {
		// TODO Auto-generated method stub
		if (StringUtils.isNotBlank(ftpServerPath)
				&& ftpServerPath.startsWith("/")) {
			ftpServerPath = StringUtils.substringAfter(ftpServerPath, "/");
		}
		Message<byte[]> message=null;
		try {
			message = MessageBuilder.withPayload(IOUtils.toByteArray(in))
					.setHeader("remote_dir", new String(ftpServerPath.getBytes(Charset.forName("UTF-8")),"ISO-8859-1"))
					.setHeader("remote_filename", new String(fileName.getBytes(Charset.forName("UTF-8")),"ISO-8859-1")).build();
		} catch (IOException e) {
			logger.error("ftp上传图片产生异常",e);
			throw new Exception(e);
		} finally{
			IOUtils.closeQuietly(in);
		}
		return ftpUploadChannel.send(message);
	}

	@Override
	public boolean deleteFile(String fileName, String ftpServerPath) throws Exception {
		// TODO Auto-generated method stub
		if (StringUtils.isNotBlank(ftpServerPath)
				&& ftpServerPath.startsWith("/")) {
			ftpServerPath = StringUtils.substringAfter(ftpServerPath, "/");
		}
		Message<String> message = null;
		try {
			message = MessageBuilder.withPayload(fileName)
					.setHeader("file_remoteDirectory", new String(ftpServerPath.getBytes(Charset.forName("UTF-8")),"ISO-8859-1"))
					.setHeader("file_remoteFile", new String(fileName.getBytes(Charset.forName("UTF-8")),"ISO-8859-1"))
					.build();
		} catch (Exception e) {
			logger.error("ftp删除图片产生异常",e);
			throw new Exception(e);
		}
		logger.info("删除ftp图片：getHeaders="+message.getHeaders());
		return ftpRemoveChannel.send(message);
	}

	
	@Override
	public boolean uploadFileSplit(InputStream in, String ftpServerPath, String fileMD5, int fileSplitNo) throws Exception {
		// TODO Auto-generated method stub
		FTPUtil ftp = new FTPUtil();   
        try {   
        	ftp.connect(ftpServerIp, ftpServerPort, ftpServerUserName, ftpServerPassword);
        	//将文件分片上传到以文件MD5命名的文件中
        	String remotePath = ftpServerPath + File.separator + fileMD5 + File.separator + fileSplitNo + ".slt";
        	ftp.upload(remotePath, in);
        } catch (IOException e) {   
            System.out.println("连接FTP出错："+e.getMessage());   
        } finally{
        	ftp.disconnect();   
        }
		return true;
	}

	@Override
	public boolean mergeFileSplit(String ftpServerPath, String fileMD5, String fileName)
			throws Exception {
		FTPUtil ftp = new FTPUtil();   
        try {   
        	ftp.connect(ftpServerIp, ftpServerPort, ftpServerUserName, ftpServerPassword);
        	//判断合并后的文件是否存在，如果存在，则不合并
        	if(ftp.isExistFile(ftpServerPath + File.separator + fileName)){
        		return true;
        	}

        	//获取文件分片列表
        	String splitDir = ftpServerPath + File.separator + fileMD5;
        	List<FTPFile> fileList = ftp.listFile(splitDir);
        	//分片排序
        	Collections.sort(fileList, new Comparator<FTPFile>(){
				@Override
				public int compare(FTPFile arg0, FTPFile arg1) {
					// TODO Auto-generated method stub
					if(arg0.getName().length()>arg1.getName().length()){
						return 1;
					}else if(arg0.getName().length()<arg1.getName().length()){
						return -1;
					}else{
						return arg0.getName().compareTo(arg1.getName());
					}
				}
        	});
        	List<String> filePathList = new ArrayList<String>();
        	String filePath = ftpServerPath + File.separator + fileName;
        	for(FTPFile file : fileList){
        		String fileItemPath = splitDir + File.separator + file.getName();
        		filePathList.add(fileItemPath);
        	}
			if(ftp.mergeFile(filePathList, filePath)){
				ftp.deleteDir(splitDir);
			}
        } catch (IOException e) {   
            System.out.println("连接FTP出错："+e.getMessage());   
        } finally{
        	ftp.disconnect();   
        }
		return true;
	}

	@Override
	public boolean checkFileExist(String ftpServerPath, String fileName)
			throws Exception {
		// TODO Auto-generated method stub
		boolean rs = false;
		FTPUtil ftp = new FTPUtil();
		ftp.connect(ftpServerIp, ftpServerPort, ftpServerUserName, ftpServerPassword);
		if(ftp.isExistFile(ftpServerPath + File.separator + fileName)){
			rs = true;
		}
		ftp.disconnect();   
		return rs;
	}

}
