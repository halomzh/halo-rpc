package com.halo.rpc.spring.serializer;

/**
 * @author shoufeng
 */

public interface Serializer {

	/**
	 * 编码
	 *
	 * @param obj 编码对象
	 * @param <T> 对象类型
	 * @return 编码结果
	 */
	<T> byte[] encode(T obj);

	/**
	 * 解码
	 *
	 * @param bytes 编码结果
	 * @return 解码对象
	 */
	Object decode(byte[] bytes);

	/**
	 * 解码
	 *
	 * @param bytes  编码结果
	 * @param tClazz 解码对象类
	 * @param <T>    解码对象类型
	 * @return 解码对象
	 */
	<T> T decode(byte[] bytes, Class<T> tClazz);

}
