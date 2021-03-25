package com.halo.rpc.spring.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 响应
 *
 * @author shoufeng
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage implements Serializable {

	private String requestId;

	private int code;

	private String message;

	private Object data;
	
}
