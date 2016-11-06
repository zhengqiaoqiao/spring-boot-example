package org.example.web.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("exception-handler")
public class ExceptionHandlerTest {
	@RequestMapping("test")
	public String test(int a) throws Exception{
		if(a==1){
			return "success";
		}else{
			throw new Exception("dkakd!");
		}
	}
}
