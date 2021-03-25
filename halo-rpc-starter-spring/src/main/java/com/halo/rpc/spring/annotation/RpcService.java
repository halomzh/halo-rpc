package com.halo.rpc.spring.annotation;

import java.lang.annotation.*;

/**
 * 服务提供
 *
 * @author shoufeng
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RpcService {
}
