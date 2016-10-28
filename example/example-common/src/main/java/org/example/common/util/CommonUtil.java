package org.example.common.util;

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;

public class CommonUtil {
	/**
	 * 打印图片文字
	 * @param text
	 * @param fontSize
	 * @param width
	 * @param height
	 * @param symbol
	 */
	public static void printImageText(String text, int fontSize, int width, int height, float maginX, float maginY, char symbol){
		try { 
			Font font = new Font("黑体", Font.PLAIN, fontSize); 
			AffineTransform at = new AffineTransform(); 
			FontRenderContext frc = new FontRenderContext(at, true, true); 
			GlyphVector gv = font.createGlyphVector(frc, text); // 要显示的文字 
			Shape shape = gv.getOutline(maginX, maginY);  
			boolean[][] view = new boolean[width][height]; 
			for (int j = 0; j < height; j++)  { 
				for (int i = 0; i < width; i++)  { 
					if (shape.contains(i, j))  { 
						System.out.print(symbol);// 替换成你喜欢的图案 
					} else  { 
						System.out.print(" "); 
					} 
				} 
				System.out.println(); 
			} 
		} catch (Exception e)  { 
			e.printStackTrace(); 
		} 
	}
}
