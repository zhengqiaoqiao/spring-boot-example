package org.example.server.demo.controller;

import org.example.server.system.exception.ExceptionCable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@RequestMapping("/exception-handler")
	@ExceptionCable
	public void testExceptionHandler() throws Exception{
		throw new Exception();
	}
}
