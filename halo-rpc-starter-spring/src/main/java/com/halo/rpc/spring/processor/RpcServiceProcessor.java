package com.halo.rpc.spring.processor;

import com.halo.rpc.spring.annotation.RpcService;
import com.halo.rpc.spring.hanlder.HaloServerHandler;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author shoufeng
 */

public class RpcServiceProcessor implements BeanPostProcessor {

	@Autowired
	private HaloServerHandler haloServerHandler;

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		RpcService rpcService = AnnotationUtils.findAnnotation(bean.getClass(), RpcService.class);
		if (ObjectUtils.isEmpty(rpcService)) {
			return bean;
		}
		Method[] methods = bean.getClass().getDeclaredMethods();
		for (Method method : methods) {
			String methodKey = beanName + "#" + method.getName() + "#" + Arrays.toString(method.getParameterTypes());
			haloServerHandler.methodKeyBeanMap.put(methodKey, bean);
			haloServerHandler.methodKeyMethodMap.put(methodKey, method);
		}

		return bean;
	}

}
