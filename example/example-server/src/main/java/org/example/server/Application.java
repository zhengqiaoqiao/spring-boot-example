package org.example.server;

import java.io.PrintStream;

import org.example.common.util.CommonUtil;
import org.example.server.system.cache.CacheProperties;
import org.example.server.system.properties.DefaultProperties;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableAutoConfiguration
@EnableConfigurationProperties({DefaultProperties.class, CacheProperties.class})
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
				CommonUtil.printImageText(APP_NAME, 20, 200, 20, 0, 20, '$');
			}
    	});
    	application.run(args);
    }
}
