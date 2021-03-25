package com.hao.rpc.example.a.service.impl;

import com.halo.rpc.spring.annotation.RpcReference;
import com.hao.rpc.example.a.service.CalculationService;
import org.springframework.stereotype.Component;

/**
 * @author shoufeng
 */

@Component
public class DemoService {

	@RpcReference(address = "127.0.0.1:3344", remoteBeanName = "calculation")
	private CalculationService calculationService;

	public int calculation(int a, int b) {
		
		return calculationService.add(a, b);
	}

}
