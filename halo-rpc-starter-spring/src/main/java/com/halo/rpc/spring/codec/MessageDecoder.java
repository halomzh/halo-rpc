package com.halo.rpc.spring.codec;

import com.halo.rpc.spring.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;


/**
 * @author shoufeng
 */

public class MessageDecoder extends ByteToMessageDecoder {

	private Class<?> messageClazz;
	private Serializer serializer;

	public MessageDecoder(Class<?> messageClazz, final Serializer serializer) {
		this.messageClazz = messageClazz;
		this.serializer = serializer;
	}

	@Override
	public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		//int类型4个字节
		if (in.readableBytes() < 4) {
			return;
		}
		//记录数据读取前位置，校验数据不合格时可以回滚
		in.markReaderIndex();
		int dataLength = in.readInt();
		if (dataLength < 0) {
			ctx.close();
		}
		//数据长度未达标
		if (in.readableBytes() < dataLength) {
			//回滚读取位置
			in.resetReaderIndex();
			return;
		}
		byte[] data = new byte[dataLength];
		in.readBytes(data);

		Object obj = serializer.decode(data, messageClazz);
		out.add(obj);
	}
	
}
