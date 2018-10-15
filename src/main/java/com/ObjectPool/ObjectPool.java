package com.ObjectPool;

import java.io.Closeable;

/*对象池的基本接口
 * 提供获取一个对象和释放一个对象的方法
 * */
public interface ObjectPool<T> extends Closeable {

	T get();

    void release(T t);
}