package com.halo.rpc.spring.serializer.impl;

import com.halo.rpc.spring.serializer.Serializer;
import com.halo.rpc.spring.utils.KryoSerializerUtils;

/**
 * @author shoufeng
 */

public class KryoSerializer implements Serializer {

	@Override
	public <T> byte[] encode(T obj) {

		return KryoSerializerUtils.instance().encode(obj);
	}

	@Override
	public Object decode(byte[] bytes) {

		return KryoSerializerUtils.instance().decode(bytes);
	}

	@Override
	public <T> T decode(byte[] bytes, Class<T> tClazz) {

		return KryoSerializerUtils.instance().decode(bytes, tClazz);
	}

}
