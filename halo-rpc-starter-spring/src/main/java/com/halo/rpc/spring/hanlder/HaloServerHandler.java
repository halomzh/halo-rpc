package com.halo.rpc.spring.hanlder;

import com.halo.rpc.spring.message.RequestMessage;
import com.halo.rpc.spring.message.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shoufeng
 */

@Slf4j
public class HaloServerHandler extends SimpleChannelInboundHandler<RequestMessage> {

	public ConcurrentHashMap<String, Object> methodKeyBeanMap = new ConcurrentHashMap<>(1024);
	public ConcurrentHashMap<String, Method> methodKeyMethodMap = new ConcurrentHashMap<>(1024);

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, RequestMessage requestMessage) throws Exception {
		log.info("服务端收到请求: {}", requestMessage);
		String requestId = requestMessage.getRequestId();
		String methodKey = requestMessage.getMethodKey();
		Object[] args = requestMessage.getArgs();
		Object bean = methodKeyBeanMap.get(methodKey);
		Method method = methodKeyMethodMap.get(methodKey);
		if (ObjectUtils.isEmpty(bean) || ObjectUtils.isEmpty(method)) {
			channelHandlerContext.writeAndFlush(new ResponseMessage(requestId, 500, "方法不存在", null));
			return;
		}

		ResponseMessage responseMessage;
		try {
			Object data = method.invoke(bean, args);
			responseMessage = new ResponseMessage(requestId, 0, "success", data);
		} catch (Exception e) {
			responseMessage = new ResponseMessage(requestId, 500, e.toString(), null);
		}

		channelHandlerContext.writeAndFlush(responseMessage);
	}

}
