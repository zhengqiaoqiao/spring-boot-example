package org.example.common.serialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class C {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, String> aaa = new HashMap<String, String>();
		aaa.put("111", "333");
		aaa.put("33", "22");
		Map<String, Map<String, String>> o = new HashMap<String, Map<String, String>>();
		
		o.put("331", aaa);
		o.put("3321", aaa);
		String ssss = JsonUtil.map2Json(o);
		Map<String, Map<String, String>> ooo = (Map<String, Map<String, String>>)JsonUtil.json2Map(ssss);
		Map<String, String> map = ooo.get("331");
		String ssq = map.get("111");
		A a = new A();
		a.setA("qq");
		a.setB("ddd");
		a.setC(11);
		List<String> d = new ArrayList<String>();
		d.add("1122");
		d.add("fsfdf");
		d.add("2344");
		a.setD(d);
		Map<String, String> e = new HashMap<String,String>();
		e.put("dfa", "1111");
		e.put("dd", "22");
		a.setE(e);
		String s = JsonUtil.bean2Json(a);
		System.out.println(s);
		A aa = JsonUtil.json2Bean(s, A.class);
		
		List<A> dDD = new ArrayList<A>();	
		dDD.add(a);
		dDD.add(a);
		dDD.add(a);
		String ss = JsonUtil.bean2Json(dDD);
		System.out.println(ss);
		
		Map<String, A> gg = new HashMap<String,A>();
		gg.put("1E", a);
		gg.put("2E", a);
		gg.put("3E", a);
		
		B b = new B();
		b.setA("ddf");
		b.setB(dDD);
		b.setC(gg);
		String sss = JsonUtil.bean2Json(b);
		System.out.println(sss);
		
		B bb = (B) JsonUtil.json2Bean(sss, B.class);
		System.out.println(bb.getC().get("1E").getA());
		
		List<B> rr = new ArrayList<B>();	
		rr.add(bb);
		rr.add(bb);
		rr.add(bb);
		String ee = JsonUtil.list2Json(rr);
		System.out.println(ee);
		
		List<B> hh = (List<B>) JsonUtil.json2List(ee, B.class);
		System.out.println(hh.get(0).getC().get("1E").getA());
		
	}

}
