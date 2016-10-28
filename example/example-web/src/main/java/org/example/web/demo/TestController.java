package org.example.web.demo;

import java.util.List;
import java.util.Map;

import org.example.common.serialize.A;
import org.example.common.serialize.B;
import org.example.common.serialize.E;
import org.example.common.serialize.JsonUtil;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class TestController {
	@RequestMapping("/test")
	public List<B> test(@RequestBody List<B> list){
		System.out.println("dd");
		return list;
//		System.out.println(list[0].getB().get(0).getA());
//		System.out.println(list[0].getC().get("1E").getB());
		
	}
	
	@RequestMapping("/test2")
	public Map<String, A> test2(@RequestBody Map<String, A> map){
		System.out.println("dd");
		return map;
//		System.out.println(list[0].getB().get(0).getA());
//		System.out.println(list[0].getC().get("1E").getB());
		
	}
	
	
	@RequestMapping("/test3")
	public void test3(@RequestBody E e){
		System.out.println("dd");
		
//		System.out.println(list[0].getB().get(0).getA());
//		System.out.println(list[0].getC().get("1E").getB());
		
	}
}
