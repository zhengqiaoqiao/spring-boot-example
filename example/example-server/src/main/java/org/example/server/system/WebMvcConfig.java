package org.example.server.system;

import org.example.server.system.interceptor.Interceptor1;
import org.example.server.system.interceptor.Interceptor2;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	 /**
	 * 拦截器
	 */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new Interceptor1())
				.addPathPatterns("/*/*.do");
    	registry.addInterceptor(new Interceptor2())
				.addPathPatterns("/*/*.do");
    }
    /**
     * 静态资源
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/mystatic/**")
                .addResourceLocations("classpath:/mystatic/");
    }

//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        registry.enableContentNegotiation(new MappingJackson2JsonView());
//        registry.freeMarker().cache(false);
//    }
//

}
