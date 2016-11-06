package org.example.web.system.bean;

import java.lang.reflect.Method;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
	@Bean
	public KeyGenerator wiselyKeyGenerator(){
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method,
					Object... params) {
				// TODO Auto-generated method stub
				StringBuilder sb = new StringBuilder();
	            sb.append(target.getClass().getName());
	            sb.append(method.getName());
	            for (Object obj : params) {
	                sb.append(obj.toString());
	            }
	            return sb.toString();
			}
		};
	}
	@Bean
	public CacheManager cacheManager(RedisTemplate redisTemplate) {
	    RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
	    // Number of seconds before expiration. Defaults to unlimited (0)
	    cacheManager.setDefaultExpiration(30 * 60); //设置key-value超时时间
	    return cacheManager;
	}
}
