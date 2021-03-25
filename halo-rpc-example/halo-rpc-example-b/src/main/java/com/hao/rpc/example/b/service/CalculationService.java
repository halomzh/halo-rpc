package com.hao.rpc.example.b.service;

import com.halo.rpc.spring.annotation.RpcService;
import org.springframework.stereotype.Component;

/**
 * @author shoufeng
 */

@RpcService
@Component("calculation")
public class CalculationService {

	public int add(int a, int b) {
		return a + b;
	}

}
