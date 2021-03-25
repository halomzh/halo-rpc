package com.halo.rpc.spring.client;

import com.halo.rpc.spring.codec.MessageDecoder;
import com.halo.rpc.spring.codec.MessageEncoder;
import com.halo.rpc.spring.message.RequestMessage;
import com.halo.rpc.spring.message.ResponseMessage;
import com.halo.rpc.spring.serializer.impl.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shoufeng
 */

@Slf4j
public class HaloRpcClient {

	public ConcurrentHashMap<String, Channel> addressChannelMap = new ConcurrentHashMap<>(1024);
	public ExecutorService rpcClientExecutorService = Executors.newCachedThreadPool();
	public ConcurrentHashMap<String, ResponseMessage> requestIdResponseMessageMap = new ConcurrentHashMap<>(1024);

	public void start(String address) {
		rpcClientExecutorService.execute(() -> {
			NioEventLoopGroup group = new NioEventLoopGroup(1);
			try {
				new Bootstrap()
						.group(group)
						.channel(NioSocketChannel.class)
						.handler(new ChannelInitializer<SocketChannel>() {
							@Override
							protected void initChannel(SocketChannel ch) throws Exception {
								ch.pipeline().addLast(new MessageDecoder(ResponseMessage.class, new KryoSerializer()));
								ch.pipeline().addLast(new MessageEncoder(RequestMessage.class, new KryoSerializer()));
								ch.pipeline().addLast(new SimpleChannelInboundHandler<ResponseMessage>() {
									@Override
									protected void channelRead0(ChannelHandlerContext channelHandlerContext, ResponseMessage responseMessage) throws Exception {
										requestIdResponseMessageMap.put(responseMessage.getRequestId(), responseMessage);
									}

									@Override
									public void channelActive(ChannelHandlerContext ctx) throws Exception {
										synchronized (HaloRpcClient.class) {
											Channel channel = ctx.channel();
											addressChannelMap.put(address, channel);
										}
										super.channelActive(ctx);
									}
								});
							}
						}).connect(address.split(":")[0], Integer.parseInt(address.split(":")[1])).channel().closeFuture().sync();
			} catch (InterruptedException e) {
				log.info("启动连接失败address[{}]: {}", address, e);
			} finally {
				group.shutdownGracefully();
			}
		});
	}

}
