package org.example.common.serialize;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class D implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = null;
    private Map<String, Object> map = null;
    private List<String> list = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", map=" + map +
                ", list=" + list +
                '}';
    }
}
