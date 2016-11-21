package org.example.server.ftp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class DownloadHttp {

	
	public static String formatSize(Long fileSize) {
		String formatSize = "";
		DecimalFormat df = new DecimalFormat("0.##");
		float fsize = Float.parseFloat(fileSize.toString());
		if (fileSize / (1024 * 1024 * 1024) > 0) {
			formatSize = df.format(fsize/ (1024 * 1024 * 1024))+ " GB";
		} else if (fileSize / (1024 * 1024) > 0) {
			formatSize = df.format(fsize/ (1024 * 1024))+ " MB";
		} else if (fileSize / (1024) > 0) {
			formatSize = df.format(fsize/ (1024))+ " KB";
		} else {
			formatSize = fileSize + " 字节";
		}
		return formatSize;
	}

	
	/**
	 * 断点续传HTTP模式资源
	 * @param srcHttpFile
	 * @param destFile
	 * @return
	 */
	public static long httpDownload(String srcHttpFile, String destFile) {
		srcHttpFile = srcHttpFile.replaceAll("\\\\", "/");
		destFile = destFile.replaceAll("\\\\", "/");

		// 下载网络文件
		long bytesum = 0;
		int byteread = 0;
		InputStream is = null;
		RandomAccessFile raf = null;
		HttpURLConnection httpConnection = null;
		long fileSize = 0;
		try {
			String fileName = srcHttpFile.substring(srcHttpFile.lastIndexOf('/')+1);  
			URL url = new URL(srcHttpFile);
			httpConnection = (HttpURLConnection) url.openConnection();
			File nfile = new File(destFile);
			if(nfile.exists()){
				fileSize = nfile.length();
			}else{
				nfile.createNewFile();
			}
			byte[] buffer = new byte[1024 * 1024];
			httpConnection.setRequestProperty("User-Agent", "NetFox");
			httpConnection.setRequestProperty("RANGE", "bytes=" +  fileSize + "-");
			
			if(httpConnection.getContentLength() == 0 ){
				System.out.println("http download progress： 100% ("+fileName+","+formatSize(fileSize+bytesum)+")");  
				return fileSize;
			}
			
			is = httpConnection.getInputStream();

			raf = new RandomAccessFile(destFile, "rw");
			raf.seek(fileSize);

		    long step = (fileSize + httpConnection.getContentLength())/100;   
	        long process= (fileSize + bytesum) /step;
	        
	        System.out.println("http download progress： "+process +"% ("+fileName+","+formatSize(fileSize+bytesum)+")");   
	        
			while ((byteread = is.read(buffer)) != -1) {
				bytesum += byteread;
				raf.write(buffer, 0, byteread);

				long nowProcess = (fileSize+bytesum) /step;   
	            if(nowProcess > process){   
	                 process = nowProcess;   
	                 System.out.println("http download progress： "+process +"% ("+fileName+","+formatSize(fileSize+bytesum)+")");   
	            }   
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -2;
		} finally {
			try {
				if (raf != null) {
					raf.close();
				}
				if (is != null) {
					is.close();
				}
				if (httpConnection != null) {
					httpConnection.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return bytesum;
	}
	
	public static void main(String[] args) {
		String src="http://repo.spring.io/libs-release-local/org/springframework/spring/4.1.0.RELEASE/spring-framework-4.1.0.RELEASE-dist.zip";
		httpDownload(src,"D:/spring-framework-4.1.0.RELEASE-dist.zip");
	}

}