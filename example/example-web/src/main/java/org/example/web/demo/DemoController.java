package org.example.web.demo;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.example.api.demo.service.IDemoServiceApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("demo")
public class DemoController {
	@Resource
	private IDemoServiceApi demoServiceApi;
	@RequestMapping("test")
	public String test(String msg) throws Exception{
		String rs = demoServiceApi.test(msg);
		return rs;
	}
	@RequestMapping("test2")
	public String test2(String msg) throws Exception{
		return msg;
	}
	@RequestMapping("get")
	public Map<String, Object> get() throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("a", "hello");
		result.put("b", "world!");
		return result;
	}
}
