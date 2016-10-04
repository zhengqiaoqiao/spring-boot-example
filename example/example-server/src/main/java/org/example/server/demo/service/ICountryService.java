package org.example.server.demo.service;

import java.util.List;

import org.example.server.demo.bean.Country;


public interface ICountryService {
	public Country getCountryById(String id);
	
	public List<Country> getCountryByPage(int pageNum, int pageSize);
}
