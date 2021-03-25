package com.halo.rpc.spring.processor;

import com.halo.rpc.spring.annotation.RpcReference;
import com.halo.rpc.spring.client.HaloRpcClient;
import com.halo.rpc.spring.message.RequestMessage;
import com.halo.rpc.spring.message.ResponseMessage;
import io.netty.channel.Channel;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author shoufeng
 */

public class RpcReferenceProcessor implements BeanPostProcessor {

	@Autowired
	private HaloRpcClient haloRpcClient;

	@SneakyThrows
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class<? extends Object> clazz = bean.getClass();
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field declaredField : declaredFields) {
			declaredField.setAccessible(true);
			if (!declaredField.isAnnotationPresent(RpcReference.class)) {
				continue;
			}
			if (!declaredField.getType().isInterface()) {
				throw new RuntimeException("@RpcReference注解只能应用在接口类型的字段上");
			}
			RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
			String address = rpcReference.address();
			Object value = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{declaredField.getType()}, new InvocationHandler() {
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					String methodKey = rpcReference.remoteBeanName() + "#" + method.getName() + "#" + Arrays.toString(method.getParameterTypes());
					Channel channel = haloRpcClient.addressChannelMap.get(address);
					if (ObjectUtils.isEmpty(channel)) {
						synchronized (RpcReferenceProcessor.class) {
							haloRpcClient.start(address);
							while (ObjectUtils.isEmpty(channel)) {
								channel = haloRpcClient.addressChannelMap.get(address);
							}
						}
					}
					String requestId = UUID.randomUUID().toString();
					RequestMessage requestMessage = new RequestMessage(requestId, methodKey, args);
					channel.writeAndFlush(requestMessage);
					while (ObjectUtils.isEmpty(haloRpcClient.requestIdResponseMessageMap.get(requestId))) {
					}
					ResponseMessage responseMessage = haloRpcClient.requestIdResponseMessageMap.get(requestId);
					haloRpcClient.requestIdResponseMessageMap.remove(requestId);
					if (responseMessage.getCode() != 0) {
						throw new RuntimeException(responseMessage.getMessage());
					}
					return responseMessage.getData();
				}
			});
			declaredField.set(bean, value);
		}
		return bean;
	}

}
