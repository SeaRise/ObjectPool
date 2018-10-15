package com.ObjectPool.impl;

import com.ObjectPool.Validator;

/*这个是默认的对象验证器
 * */
public class DefaultValidator<T> implements Validator<T> {

	public boolean isValid(T t) {
		return true;
	}

	public void invalidate(T t) {
	}

}
