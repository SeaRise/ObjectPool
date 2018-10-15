package com.ObjectPool;

import java.io.IOException;

/*基础对象池的实现
 * */
public abstract class BaseObjectPool<T> implements ObjectPool<T>{

	protected Validator<T> validator = null;
	
	private volatile boolean isClosed = false;
	
	public void release(T t) {
		if (validator.isValid(t)) {
			returnToPool(t);
		} else {
			validator.invalidate(t);
		}
	}
	
	public void close() throws IOException {
		isClosed = false;
		closeResource();
	}
	
	protected boolean isClosed() {
		return isClosed;
	}
	
	protected void closeResource() {
		validator = null;
	}
	
	protected abstract void returnToPool(T t);
	
}
