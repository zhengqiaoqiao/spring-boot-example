package org.example.server.ftp.service;

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
	
	
	/**
	 * 上传文件分片
	 * @param in
	 * @param ftpServerPath
	 * @param fileMD5
	 * @param fileSplitNo
	 * @return
	 * @throws Exception 
	 */
	public boolean uploadFileSplit(InputStream in, String ftpServerPath, String fileMD5, int fileSplitNo) throws Exception;
	
	/**
	 * 合并文件分片
	 * @param ftpServerPath
	 * @param fileMD5
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public boolean mergeFileSplit(String ftpServerPath, String fileMD5, String fileName) throws Exception;
	/**
	 * 判断文件是否存在
	 * @param ftpServerPath
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public boolean checkFileExist(String ftpServerPath, String fileName) throws Exception;
}
