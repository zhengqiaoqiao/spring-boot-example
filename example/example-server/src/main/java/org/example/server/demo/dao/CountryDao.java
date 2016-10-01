package org.example.server.demo.dao;

import org.example.server.demo.bean.Country;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryDao {
	public Country getCountryById(String id);
}
