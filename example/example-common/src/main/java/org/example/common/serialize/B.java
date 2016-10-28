package org.example.common.serialize;

import java.util.List;
import java.util.Map;

public class B {
	private String a;
	private List<A> b;
	private Map<String, A> c;
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public List<A> getB() {
		return b;
	}
	public void setB(List<A> b) {
		this.b = b;
	}
	public Map<String, A> getC() {
		return c;
	}
	public void setC(Map<String, A> c) {
		this.c = c;
	}
}
