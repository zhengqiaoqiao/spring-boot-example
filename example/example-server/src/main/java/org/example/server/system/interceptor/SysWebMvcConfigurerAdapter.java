package org.example.server.system.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * spring mvc配置（拦截器）
 * @author QIAO
 *
 */
@Configuration
public class SysWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new Interceptor1())
				.addPathPatterns("/*");
    	registry.addInterceptor(new Interceptor2())
				.addPathPatterns("/*");
    }
}