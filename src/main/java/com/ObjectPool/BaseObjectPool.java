package com.ObjectPool;

/*基础对象池的实现
 * */
public abstract class BaseObjectPool<T> implements ObjectPool<T>{

	public void release(T t) {
		if (isValid(t)) {
			returnToPool(t);
		} else {
			invalidate(t);
		}
	}

	protected abstract boolean isValid(T t);

	protected abstract void invalidate(T t);
	
	protected abstract void returnToPool(T t);
	
}
