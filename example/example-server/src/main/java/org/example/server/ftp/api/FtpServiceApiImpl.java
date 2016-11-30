package org.example.server.ftp.api;

import java.io.InputStream;

import javax.annotation.Resource;

import org.example.api.ftp.service.IFtpServiceApi;
import org.example.server.ftp.service.IFtpService;
import org.springframework.stereotype.Service;

@Service
public class FtpServiceApiImpl implements IFtpServiceApi{
	@Resource
	private IFtpService ftpService;
	@Override
	public boolean uploadFile(InputStream in, String ftpServerPath,
			String fileName) throws Exception {
		// TODO Auto-generated method stub
		return ftpService.uploadFile(in, ftpServerPath, fileName);
	}

	@Override
	public boolean deleteFile(String fileName, String ftpServerPath)
			throws Exception {
		// TODO Auto-generated method stub
		return ftpService.deleteFile(fileName, ftpServerPath);
	}

	@Override
	public boolean uploadFileSplit(InputStream in, String ftpServerPath,
			String fileMD5, int fileSplitNo) throws Exception {
		// TODO Auto-generated method stub
		return ftpService.uploadFileSplit(in, ftpServerPath, fileMD5, fileSplitNo);
	}

	@Override
	public boolean mergeFileSplit(String ftpServerPath, String fileMD5,
			String fileName) throws Exception {
		// TODO Auto-generated method stub
		return ftpService.mergeFileSplit(ftpServerPath, fileMD5, fileName);
	}

	@Override
	public boolean checkFileExist(String ftpServerPath, String fileName)
			throws Exception {
		// TODO Auto-generated method stub
		return ftpService.checkFileExist(ftpServerPath, fileName);
	}

}
