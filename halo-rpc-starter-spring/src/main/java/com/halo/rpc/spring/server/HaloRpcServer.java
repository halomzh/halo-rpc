package com.halo.rpc.spring.server;

import com.halo.rpc.spring.codec.MessageDecoder;
import com.halo.rpc.spring.codec.MessageEncoder;
import com.halo.rpc.spring.hanlder.HaloServerHandler;
import com.halo.rpc.spring.message.RequestMessage;
import com.halo.rpc.spring.message.ResponseMessage;
import com.halo.rpc.spring.serializer.impl.KryoSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shoufeng
 */

public class HaloRpcServer {

	@Autowired
	private HaloServerHandler haloServerHandler;

	public ExecutorService rpcServerExecutorService = Executors.newCachedThreadPool();

	public void start(int port) {
		rpcServerExecutorService.execute(() -> {
			NioEventLoopGroup boosGroup = new NioEventLoopGroup();
			NioEventLoopGroup workerGroup = new NioEventLoopGroup();
			try {
				ServerBootstrap serverBootstrap = new ServerBootstrap();
				serverBootstrap
						.group(boosGroup, workerGroup)
						.channel(NioServerSocketChannel.class)
						.option(ChannelOption.SO_BACKLOG, 128)
						.childOption(ChannelOption.SO_KEEPALIVE, true)
						.childHandler(new ChannelInitializer<SocketChannel>() {
							@Override
							protected void initChannel(SocketChannel ch) throws Exception {
								ch.pipeline().addLast(new MessageDecoder(RequestMessage.class, new KryoSerializer()));
								ch.pipeline().addLast(new MessageEncoder(ResponseMessage.class, new KryoSerializer()));
								ch.pipeline().addLast(new IdleStateHandler(5, 5, 5));
								ch.pipeline().addLast(haloServerHandler);
							}
						}).bind(port).sync().channel().closeFuture().sync();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} finally {
				boosGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			}
		});
	}

}
