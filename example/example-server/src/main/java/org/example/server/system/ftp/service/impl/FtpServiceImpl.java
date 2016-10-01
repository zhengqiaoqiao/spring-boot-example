package org.example.server.system.ftp.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.example.server.system.ftp.service.IFtpService;
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

}
