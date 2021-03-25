package com.hao.rpc.example.a;

import com.hao.rpc.example.a.service.impl.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shoufeng
 */

@SpringBootApplication
@RestController
@RequestMapping("/example")
@Slf4j
public class App {

	@Autowired
	private DemoService demoService;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@GetMapping("/calculation")
	public int calculation() {

		return demoService.calculation(1, 2);
	}

}
