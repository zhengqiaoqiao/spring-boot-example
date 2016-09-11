package org.example.web;

import org.example.api.user.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RestTemplate restTemplate = new RestTemplate();
		UserResponse rs = restTemplate.getForObject("http://localhost:8080/get-user.do", UserResponse.class);
		logger.info(rs.toString());
	}

}
