package org.example.server;

import org.example.server.system.upload.StorageProperties;
import org.example.server.system.upload.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableAutoConfiguration
@EnableConfigurationProperties(StorageProperties.class)
@ImportResource({
//	"classpath:spring-mybatis.xml",
	"classpath:spring-oval.xml",
//	"classpath:spring-dubbo-provider.xml",
	"classpath:spring-ftp.xml"
})
@ServletComponentScan
public class Application extends WebMvcConfigurerAdapter{

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
            storageService.deleteAll();
            storageService.init();
		};
	}
}
