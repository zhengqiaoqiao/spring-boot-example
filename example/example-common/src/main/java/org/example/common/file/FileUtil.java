package org.example.common.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.StringUtils;

/**
 * <p>Title: FileUtil</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2016年11月22日
 */
public class FileUtil {
	/**
	 * 递归方法
	 * @param path 文件路径
	 */
	public static List<File> find(String path){
		List<File> fileList = new ArrayList<File>();
		File file=new File(path);
		if(!file.exists()){
			return fileList;
		}
		if(file.isDirectory()){
			File[] files = file.listFiles();
			//如果文件数组为null则返回
	        if (files != null){
	        	for (int i = 0; i < files.length; i++) { 
	                if (files[i].isDirectory()) { 
	                	//判断是不是文件夹，如果是文件夹则继续向下查找文件
	                	find(files[i].getAbsolutePath()); 
	                } else { 
	                    fileList.add(files[i]);
	                } 
	            } 
	        }
		}else{
			fileList.add(file);
		}
        return fileList;
	}
	/**
	 * 获取真实的文件路径（windows linux兼容）
	 * @param path
	 * @param os  为空时，根据运行机子自行判断
	 * @return
	 */
	public static String getRealFilePath(String path, String os) {
		if(os==null || os.equals("")){
			path = path.replace("/", File.separator).replace("\\", File.separator);  
		}else if(os.equals("windows")){
			path = path.replace("/", "\\");  
		}else if(os.equals("linux")){
			path = path.replace("\\", "/");  
		}
		return path;
    }  
	
	
}
