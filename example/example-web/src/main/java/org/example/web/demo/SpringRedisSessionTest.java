package org.example.web.demo;

import javax.servlet.http.HttpServletRequest;


import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("redis-session")
public class SpringRedisSessionTest {
	
	@RequestMapping("/set")
	public void setSession(HttpServletRequest reuqest){
		HttpSession session = reuqest.getSession();
		session.setAttribute("user-info", "zheng.qq");
		
	}
	
	@RequestMapping("/get")
	public String getSession(HttpServletRequest reuqest){
		HttpSession session = reuqest.getSession();
		return (String) session.getAttribute("user-info");
	}
}

