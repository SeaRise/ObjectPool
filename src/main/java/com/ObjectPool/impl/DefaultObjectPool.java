package com.ObjectPool.impl;

import com.ObjectPool.ObjectFactory;
import com.ObjectPool.ObjectPool;
import com.ObjectPool.Validator;

/*这个是默认的对象池实现
 * 空闲池是LocalObjectPool
 * 废弃池是WeakReferencePool
 * **/
public class DefaultObjectPool<T> extends LocalObjectPool<T> {

	private ObjectPool<T> abandonedPool = null;
	
	public DefaultObjectPool(int size, ObjectFactory<T> objectFactory) {
		this(size, objectFactory, new DefaultValidator<T>());
	}
	
	public DefaultObjectPool(int size, ObjectFactory<T> objectFactory, Validator<T> validator) {
		this(size, size, objectFactory, validator);
	}
	
	public DefaultObjectPool(int localSize, int abandonedSize, ObjectFactory<T> objectFactory, Validator<T> validator) {
		super(localSize, objectFactory, validator);
		abandonedPool = new WeakReferencePool<T>(abandonedSize);
	}
	
	@Override
	protected T createNew() {
		T object = abandonedPool.get();
		return null == object ? super.createNew() : object;
	}
	
	@Override
	protected void abandonObject(T t) {
		abandonedPool.release(t);
	}
}
