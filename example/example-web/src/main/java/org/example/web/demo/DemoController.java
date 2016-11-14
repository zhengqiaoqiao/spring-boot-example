package org.example.web.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("demo")
public class DemoController {
	@RequestMapping("get")
	public Map<String, Object> get() throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("a", "hello");
		result.put("b", "world!");
		return result;
	}
}
