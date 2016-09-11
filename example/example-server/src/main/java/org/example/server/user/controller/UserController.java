package org.example.server.user.controller;

import org.example.api.user.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	@RequestMapping("/get-user")
    public UserResponse getUserById(@RequestParam(value="id", defaultValue="-1") String id) {
		logger.trace("this is trace msg.");
		logger.debug("this is debug msg.");
		logger.info("this is info msg.");
		logger.warn("this is warn msg.");
		logger.error("this is error msg.");
        UserResponse rs = new UserResponse();
        return rs;
    }
}
