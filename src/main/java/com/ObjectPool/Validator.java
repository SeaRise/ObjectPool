package com.ObjectPool;

public interface Validator<T> {
	boolean isValid(T t);

	void invalidate(T t);
}
