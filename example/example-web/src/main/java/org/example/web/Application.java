package org.example.web;

import org.example.web.system.properties.WebProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@SpringBootApplication
@EnableConfigurationProperties({WebProperties.class})
@EnableRedisHttpSession(redisNamespace="redis-session", maxInactiveIntervalInSeconds=60*60)
@ImportResource({
	"classpath:spring-dubbo-consumer.xml"
})
public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("example-web启动！");
    }
   
}
