package com.halo.rpc.spring.annotation;

import java.lang.annotation.*;

/**
 * 服务引用
 *
 * @author shoufeng
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RpcReference {

	String address() default "";

	String remoteBeanName() default "";
}
