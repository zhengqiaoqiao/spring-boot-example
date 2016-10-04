package org.example.server.demo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.sf.oval.constraint.NotEmpty;

import org.example.server.demo.bean.Country;
import org.example.server.demo.dao.CountryDao;
import org.example.server.demo.service.ICountryService;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;


@Service
public class CountryServiceImpl implements ICountryService{
	@Resource
	private CountryDao countryDao;
	@Override
	public Country getCountryById(@NotEmpty String id) {
		// TODO Auto-generated method stub
		return countryDao.getCountryById(id);
	}
	@Override
	public List<Country> getCountryByPage(int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List<Country> list = countryDao.getCountryByPage();
		return list;
	}

}
