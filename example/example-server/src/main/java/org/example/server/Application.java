package org.example.server;

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.io.PrintStream;

import org.example.server.system.upload.StorageProperties;
import org.example.server.system.upload.service.StorageService;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableAutoConfiguration
@EnableConfigurationProperties(StorageProperties.class)
@ImportResource({
	"classpath:spring-mybatis.xml",
	"classpath:spring-oval.xml",
//	"classpath:spring-dubbo-provider.xml",
	"classpath:spring-ftp.xml"
})
@ServletComponentScan
public class Application extends WebMvcConfigurerAdapter{
	private static String APP_NAME = "QIAO-OS-DEMO";
    public static void main(String[] args) {
    	SpringApplication application = new SpringApplication(Application.class);  
    	application.setBanner(new Banner(){

			@Override
			public void printBanner(Environment environment, Class<?> class1,
					PrintStream printstream) {
				try { 
					Font font = new Font("黑体", Font.PLAIN, 20); 
					AffineTransform at = new AffineTransform(); 
					FontRenderContext frc = new FontRenderContext(at, true, true); 
					GlyphVector gv = font.createGlyphVector(frc, APP_NAME); // 要显示的文字 
					Shape shape = gv.getOutline(5, 30); 
					int weith = 200; 
					int height = 40; 
					boolean[][] view = new boolean[weith][height]; 
					for (int i = 0; i < weith; i++)  { 
						for (int j = 0; j < height; j++)  { 
							if (shape.contains(i, j))  { 
								view[i][j] = true; 
							} else  { 
								view[i][j] = false; 
							} 
						} 
					} 
					for (int j = 0; j < height; j++)  { 
						for (int i = 0; i < weith; i++)  { 
							if (view[i][j])  { 
								System.out.print("@");// 替换成你喜欢的图案 
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
    		
    	});
    	application.run(args);
        
    }
    @Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
            storageService.deleteAll();
            storageService.init();
		};
	}
}
