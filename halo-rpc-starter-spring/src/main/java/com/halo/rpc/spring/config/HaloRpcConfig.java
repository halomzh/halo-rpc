package com.halo.rpc.spring.config;

import com.halo.rpc.spring.client.HaloRpcClient;
import com.halo.rpc.spring.hanlder.HaloServerHandler;
import com.halo.rpc.spring.processor.RpcReferenceProcessor;
import com.halo.rpc.spring.processor.RpcServiceProcessor;
import com.halo.rpc.spring.server.HaloRpcServer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author shoufeng
 */

@Configuration
@ConditionalOnProperty(prefix = HaloRpcConfig.PREFIX, value = "enable")
@Slf4j
@Data
public class HaloRpcConfig implements ApplicationRunner {

	public static final String PREFIX = "halo.rpc";

	@Value("${halo.rpc.port}")
	private int port;

	@Lazy
	@Autowired
	private HaloRpcServer haloRpcServer;

	@Bean
	public HaloRpcClient haloRpcClient() {

		return new HaloRpcClient();
	}

	@Bean
	public HaloServerHandler haloServerHandler() {
		return new HaloServerHandler();
	}

	@Bean
	public RpcReferenceProcessor rpcReferenceProcessor() {

		return new RpcReferenceProcessor();
	}

	@Bean
	public RpcServiceProcessor rpcServiceProcessor() {

		return new RpcServiceProcessor();
	}

	@Bean
	public HaloRpcServer haloRpcServer() {

		return new HaloRpcServer();
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		haloRpcServer.start(port);
	}

}
