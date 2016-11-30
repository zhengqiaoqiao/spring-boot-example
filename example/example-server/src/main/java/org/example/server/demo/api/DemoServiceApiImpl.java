package org.example.server.demo.api;

import org.example.api.demo.service.IDemoServiceApi;
import org.springframework.stereotype.Service;
@Service
public class DemoServiceApiImpl implements IDemoServiceApi{

	@Override
	public String test(String msg) throws Exception {
		// TODO Auto-generated method stub
		String rs = msg;
		return rs;
	}

}
