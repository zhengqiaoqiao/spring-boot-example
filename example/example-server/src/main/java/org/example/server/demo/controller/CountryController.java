package org.example.server.demo.controller;

import javax.annotation.Resource;

import net.sf.oval.constraint.NotEmpty;

import org.example.api.base.Result;
import org.example.api.enums.ResultCodeEnum;
import org.example.api.user.response.UserResponse;
import org.example.server.demo.bean.Country;
import org.example.server.demo.service.ICountryService;
import org.example.server.user.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
