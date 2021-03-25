package com.halo.rpc.spring.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 请求
 *
 * @author shoufeng
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessage implements Serializable {

	private String requestId;

	private String methodKey;

	private Object[] args;

}
