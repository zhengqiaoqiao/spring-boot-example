package org.example.common.encryp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * <p>Title: MD5Util</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2016年11月22日
 */
public class MD5Util {
	/**
	 * MD5 文件加密
	 * @param file
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMD5String(FileInputStream in) throws NoSuchAlgorithmException {  
		String value = null;
		if(in == null){
			return null;
		}
		try {
			FileChannel ch = in.getChannel();  
	        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, in.available());  
	        //获得MD5摘要算法的 MessageDigest 对象  
	        MessageDigest md5 = MessageDigest.getInstance("MD5");  
	        md5.update(byteBuffer);  
	        value = bufferToHex(md5.digest());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return value;  
    }
	
	/**
	 * MD5 文件加密
	 * @param file
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMD5String(InputStream in) throws NoSuchAlgorithmException {  
		String value = null;
		if(in == null){
			return null;
		}
		try {
	        StringBuffer sb = new StringBuffer();
	        byte[] data = new byte[1024*1024*10];
	        while((in.read(data)) != -1){
	        	String md5Str = getMD5String(data);
	        	sb.append(md5Str);
	        }
	        value = getMD5String(sb.toString());   
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return value;  
    }
	
	/**
	 * MD5字节数组加密
	 * @param bytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMD5String(byte[] bytes) throws NoSuchAlgorithmException {  
		//获得MD5摘要算法的 MessageDigest 对象  
        MessageDigest md5 = MessageDigest.getInstance("MD5");  
        //使用指定的字节更新摘要  
        md5.update(bytes);  
        return bufferToHex(md5.digest());  
    }  
	/**
	 * MD5字符串加密
	 * @param s
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMD5String(String s) throws NoSuchAlgorithmException {  
        return getMD5String(s.getBytes());  
    }  
	
	private static String bufferToHex(byte[] bytes){
		if(bytes == null){
			return null;
		}
		//获得密文  
	    BigInteger bi = new BigInteger(1, bytes);
	    String md5code = bi.toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {  
            md5code = "0" + md5code;  
        }
        return md5code;
	}
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		
		String path="E:\\软件\\win64_11gR2_client.zip";

		File file = new File(path);
		long l2 = new Date().getTime();
		String v2 = getMD5String(new FileInputStream(file));
		long l3 = new Date().getTime();
		System.out.println(l3-l2);
		System.out.println("MD5:"+v2); 
	}
}
