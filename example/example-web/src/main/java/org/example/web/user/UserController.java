package org.example.web.user;

import org.example.api.user.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	private RestTemplate httpclient;
	@RequestMapping("/test")
	public UserResponse test(){
		return httpclient.getForObject("http://localhost:8080/user/get-user.do", UserResponse.class);
	}

}
