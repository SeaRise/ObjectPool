package com.ObjectPool;

/*对象工厂的基本接口
 * 作为对象池的参数传入,提供生成一个对象的方法
 * */
public interface ObjectFactory<T> {
	T createNew();
}
