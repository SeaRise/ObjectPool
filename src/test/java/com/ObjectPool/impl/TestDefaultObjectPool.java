package com.ObjectPool.impl;

import org.junit.Assert;
import org.junit.Test;

import com.ObjectPool.ObjectFactory;


public class TestDefaultObjectPool {
	private DefaultObjectPool<String> pool = null;
	
	public TestDefaultObjectPool() {
		ObjectFactory<String> factory = new ObjectFactory<String>() {
			public String createNew() {
				return "add";
			}
		};
		
		pool = new DefaultObjectPool<String>(5, factory);
	}
	
	@Test
	public void testGet() {
		for (int i = 0; i < 10; i++) {
			Assert.assertNotNull(pool.get());
		}
	}
	
	@Test
	public void testRelease() {
		for (int i = 0; i < 10; i++) {
			pool.release("release");
		}
		for (int i = 0; i < 10; i++) {
			Assert.assertNotNull(pool.get());
		}
	}
}
