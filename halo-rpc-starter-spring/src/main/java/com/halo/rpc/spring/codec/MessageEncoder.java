package com.halo.rpc.spring.codec;

import com.halo.rpc.spring.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * @author shoufeng
 */

public class MessageEncoder extends MessageToByteEncoder<Object> {

	private Class<?> messageClazz;
	private Serializer serializer;

	public MessageEncoder(Class<?> messageClazz, final Serializer serializer) {
		this.messageClazz = messageClazz;
		this.serializer = serializer;
	}

	@Override
	public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
		if (messageClazz.isInstance(in)) {
			byte[] data = serializer.encode(in);
			//4个字节int，标记消息长度
			out.writeInt(data.length);
			out.writeBytes(data);
		}
	}

}