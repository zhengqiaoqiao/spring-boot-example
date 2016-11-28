package org.example.common.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;


/**
 * <p>Title: ImageUtils</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2016年7月15日
 */
public class ImageUtil {
	
	/**
	 * 强制压缩/放大图片到固定的大小
	 * @param w int 新宽度
	 * @param h int 新高度
	 */
	public static InputStream resize(InputStream stream, int w, int h) throws IOException {
		BufferedImage img = ImageIO.read(stream);      // 构造Image对象
		int width = img.getWidth(null);    // 得到源图宽
		int height = img.getHeight(null);  // 得到源图长
		if (width / height > w / h) {
			h = (int) (height * w / width);
		} else {
			w = (int) (width * h / height);
		}
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB ); 
		image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
		ByteArrayOutputStream os = new ByteArrayOutputStream();  
		ImageIO.write(image, "jpg", os);  
		InputStream is = new ByteArrayInputStream(os.toByteArray());  
		return is;
	}
	
	
}
