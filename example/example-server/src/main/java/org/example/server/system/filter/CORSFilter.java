package org.example.server.system.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * <p>Title: CORSFilter</p>
 * <p>Description: CORS，CrossOrigin Resources Sharing，也即跨源资源共享，是 HTML5 的一项特性，它定义了一种浏览器和服务器交互的方式来确定是否允许跨域请求。</p>
 * @author: zheng.qq
 * @date: 2016年3月18日
 */
@WebFilter(filterName="CORSFilter",urlPatterns="/*")
public class CORSFilter implements Filter{
	private final Logger LOGGER = Logger.getLogger(CORSFilter.class);

	public void destroy() {
		// TODO Auto-generated method stub
		
	}


	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		LOGGER.info("cors:跨域处理！");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
	    response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
	    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Access-Control-Allow-Methods, Access-Control-Allow-Headers, Authorization, X-Requested-With");
	    chain.doFilter(req, res);
	}


	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
