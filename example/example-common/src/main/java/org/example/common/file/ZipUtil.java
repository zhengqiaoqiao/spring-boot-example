package org.example.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>Title: ZipUtil</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2016年11月23日
 */
public class ZipUtil {
	/**
	 * 文件打包
	 * @param fileName
	 * @param out
	 * @throws IOException
	 */
	private static void zipFile(File file, ZipOutputStream out){
        if(file.exists()){
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				out.putNextEntry(new ZipEntry(file.getName()));
				int len = 0 ;
	            //读入需要下载的文件的内容，打包到zip文件    
	            while ((len = fis.read(buffer)) > 0) {
	                out.write(buffer, 0, len);
	            }
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				try {
					out.flush();
					out.closeEntry();
					if(fis!=null){
						fis.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        }
    }
	
	/**
	 * 文件打包
	 * @param fileName
	 * @param out
	 * @throws IOException
	 */
	public static void zipFile(List<File> files, String zipFilePath){
        ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(zipFilePath));
			for(File file : files){
	        	zipFile(file, out);
	        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    }
}
