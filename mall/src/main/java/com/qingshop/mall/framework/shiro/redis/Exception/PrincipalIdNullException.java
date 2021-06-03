package com.qingshop.mall.framework.shiro.redis.Exception;

@SuppressWarnings("serial")
public class PrincipalIdNullException extends RuntimeException {

	private static final String MESSAGE = "Principal Id shouldn't be null!";

	@SuppressWarnings("rawtypes")
	public PrincipalIdNullException(Class clazz, String idMethodName) {
		super(clazz + " id field: " + idMethodName + ", value is null\n" + MESSAGE);
	}
}
