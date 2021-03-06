package org.example.server.demo.controller;

import javax.annotation.Resource;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import org.example.api.base.Result;
import org.example.api.enums.ResultCodeEnum;
import org.example.server.demo.bean.Country;
import org.example.server.demo.service.ICountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("country")
public class CountryController {
	private final Logger logger = LoggerFactory.getLogger(CountryController.class);
	@Resource
	private ICountryService countryService;
	@RequestMapping("/get-country")
    public Result<Country> getCountryById(@RequestParam(value="id") @NotEmpty String id) {
		logger.info("查询国家，id:{}", id);
		Country country = countryService.getCountryById(id);
		logger.info("国家信息:{}", country);
        return Result.create(ResultCodeEnum.SUCCESS.getKey(), "success", country);
    }
	
	@RequestMapping("/get-countries-by-page")
    public Result<PageInfo<Country>> getCountryByPage(@NotEmpty @NotNull Integer pageNum, @NotEmpty @NotNull Integer pageSize) {
		logger.info("分页条件，pageNum:{}, pageSize:{}", pageNum, pageSize);
		PageInfo<Country> list = new PageInfo<Country> (countryService.getCountryByPage(pageNum, pageSize));
		logger.info("查询结果:{}", list);
        return Result.create(ResultCodeEnum.SUCCESS.getKey(), "success", list);
    }

}
