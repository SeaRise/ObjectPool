package com.ObjectPool.impl;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import com.ObjectPool.BaseObjectPool;
import com.ObjectPool.ObjectFactory;

public class DefaultObjectPool<T> extends BaseObjectPool<T> {

	private ThreadLocal<Deque<T>> objects = null;
	
	private ObjectFactory<T> objectFactory = null;
	
	private volatile boolean isClosed;
	
	//这个是每个线程私有对象池的大小,而不是整个对象池的大小
	private int localSize;
	
	public DefaultObjectPool(int size, ObjectFactory<T> objectFactory) {
		super();
		this.localSize = size;
		this.objectFactory = objectFactory;
		objects = new ThreadLocal<Deque<T>>();
	}
	
	public T get() {
		checkClose();
		Deque<T> localObjects = objects.get();
		if (null == localObjects) {
			localObjects = new ArrayDeque<T>(localSize);
			objects.set(localObjects);
			return objectFactory.createNew();
		}
		
		T object = localObjects.pollFirst();
		return null == object? objectFactory.createNew() : object;
	}
	
	public void release(T t) {
		if (!isClosed) {
			super.release(t);
		}
	}

	private void checkClose() {
		if (isClosed) {
			throw new IllegalStateException("Object pool is already shutdown");
		}
	}
	
	public void close() throws IOException {
		isClosed = false;
		objectFactory = null;
		objects = null;
	}

	@Override
	protected boolean isValid(T t) {
		return true;
	}

	@Override
	protected void invalidate(T t) {
		
	}

	@Override
	protected void returnToPool(T t) {
		Deque<T> localObjects = objects.get();
		
		if (null == localObjects) {
			localObjects = new ArrayDeque<T>(localSize);
			objects.set(localObjects);
		}
		
		if (localSize == localObjects.size()) {
			return;
		}
		
		localObjects.offerLast(t);
	}
	
	public int getLocalSize() {
		return localSize;
	}
}
