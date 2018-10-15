package com.ObjectPool.impl;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import com.ObjectPool.BaseObjectPool;
import com.ObjectPool.Validator;
import com.ObjectPool.impl.WeakReferencePool.Entry;

/*这个弱引用的对象池实现,目的是用来存储被线程私有对象池丢弃的对象
 * 在线程私有对象池没有对象可取时,可以从这个弱引用对象池里拿
 * 这个是线程共享的.
 * 不保证可以返回对象
 * */
public class WeakReferencePool<T> extends BaseObjectPool<T> {
	
	private int size;
	
	private Entry<T>[] objects = null;

	private Random random = null;
	
	public WeakReferencePool(int size) {
		this(size, new DefaultValidator<T>());
	}
	
	@SuppressWarnings({ })
	public WeakReferencePool(int size, Validator<T> validator) {
		super();
		this.size = size;
		this.validator = validator;
		random = new Random();
		initObjects();
	}
	
	@SuppressWarnings("unchecked")
	private void initObjects() {
		objects = (Entry<T>[])new Entry[size];
		for (int i = 0; i < objects.length; i++) {
			objects[i] = new Entry<T>();
		}
	}
	
	public T get() {
		return objects[getIndex()].get();
	}

	@Override
	protected void returnToPool(T t) {
		objects[getIndex()].set(t);
	}
	
	@Override
	protected void closeResource() {
		objects = null;
		random = null;
	}
	
	private int getIndex() {
		return random.nextInt(size);
	}
	
	@SuppressWarnings({ "serial", "hiding" })
	class Entry<T> extends ReentrantLock {
		WeakReference<T> ref = new WeakReference<T>(null);
		
		void set(T t) {
			if (tryLock()) {
				ref = new WeakReference<T>(t);
				unlock();
			}
		}
		
		T get() {
			if (tryLock()) {
				T t = ref.get();
				ref.clear();
				unlock();
				return t;
			}
			return null;
		}
		
	}
}
