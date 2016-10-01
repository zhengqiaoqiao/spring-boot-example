package org.example.server.system.ftp.service;

import java.io.InputStream;


public interface IFtpService {

	/**
	 * ftp上传
	 * @param in
	 * @param ftpServerPath
	 * @param fileName
	 * @return
	 * @throws Exception 
	 */
	public boolean uploadFile(InputStream in, String ftpServerPath, String fileName) throws Exception;
	
	/**
	 * ftp删除
	 * @param fileName
	 * @param ftpServerPath
	 * @return
	 * @throws Exception 
	 */
	public boolean deleteFile(String fileName, String ftpServerPath) throws Exception;
}
