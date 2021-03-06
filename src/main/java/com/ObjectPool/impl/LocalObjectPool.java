package com.ObjectPool.impl;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import com.ObjectPool.BaseObjectPool;
import com.ObjectPool.ObjectFactory;
import com.ObjectPool.ObjectPool;
import com.ObjectPool.Validator;

/*这个是线程私有对象池实现
 * **/
public class LocalObjectPool<T> extends BaseObjectPool<T> implements ObjectPool<T> {
	
	private ThreadLocal<Deque<T>> objects = null;
	
	private ObjectFactory<T> objectFactory = null;
	
	//这个是每个线程私有对象池的大小,而不是整个对象池的大小
	private int localSize;
	
	public LocalObjectPool(int size, ObjectFactory<T> objectFactory) {
		this(size, objectFactory, new DefaultValidator<T>());
	}
	
	public LocalObjectPool(int size, ObjectFactory<T> objectFactory, Validator<T> validator) {
		super();
		this.localSize = size;
		this.objectFactory = objectFactory;
		this.validator = validator;
		objects = new ThreadLocal<Deque<T>>();
	}
	
	public T get() {
		checkClose();
		Deque<T> localObjects = objects.get();
		if (null == localObjects) {
			localObjects = new ArrayDeque<T>(localSize);
			objects.set(localObjects);
			return createNew();
		}
		
		T object = localObjects.pollFirst();
		return null == object? createNew() : object;
	}
	
	@Override
	protected void returnToPool(T t) {
		Deque<T> localObjects = objects.get();
		
		if (null == localObjects) {
			localObjects = new ArrayDeque<T>(localSize);
			objects.set(localObjects);
		}
		
		if (localSize == localObjects.size()) {
			abandonObject(t);
			return;
		}
		
		localObjects.offerLast(t);
	}
	
	public int getLocalSize() {
		return localSize;
	}
	
	protected void abandonObject(T t) {
		
	}
	
	protected T createNew() {
		return objectFactory.createNew();
	}

	@Override
	protected void closeResource() throws IOException {
		super.closeResource();
		objects = null;
		objectFactory = null;
	}
}
