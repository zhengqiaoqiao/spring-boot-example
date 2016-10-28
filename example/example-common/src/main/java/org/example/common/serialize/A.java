package org.example.common.serialize;

import java.util.List;
import java.util.Map;

public class A {
	private String a;
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getB() {
		return b;
	}
	public void setB(String b) {
		this.b = b;
	}
	public int getC() {
		return c;
	}
	public void setC(int c) {
		this.c = c;
	}
	public List<String> getD() {
		return d;
	}
	public void setD(List<String> d) {
		this.d = d;
	}
	public Map<String, String> getE() {
		return e;
	}
	public void setE(Map<String, String> e) {
		this.e = e;
	}
	private String b;
	private int c;
	private List<String> d;
	private Map<String, String> e;
}
