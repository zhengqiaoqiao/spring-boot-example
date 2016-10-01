package org.example.server.demo.bean;

import org.example.api.base.Base;

public class Country extends Base{

	private static final long serialVersionUID = -1125656317036499288L;
	private String id;
	private String name;
	private String code;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
