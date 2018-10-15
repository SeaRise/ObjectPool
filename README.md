- 一个简易的对象池实现
- commons-pool是用阻塞队列LinkedBlockingDeque实现线程池
	- LinkedBlockingDeque用lock和condition实现的,存在锁竞争问题.
- 考虑用每个线程维持自己的私有对象池,类似ThreadLocal的实现,这样就不存在锁竞争问题
	- 个人感觉这样实现的前提条件是对象资源比线程多的多.
	- 其次,线程的运行使用线程池,不然私有对象池代价太大.
	

- DefaultObjectPool的实现分为两个部分
	- 空闲池
		- 采用上文所说的,用ThreadLocal实现线程私有对象池,具体结构是ArrayDequeue
	- 废弃池
		- 非线程私有,是线程共享
		- 采用Entry数组保存对象,Entry具有WeakReference和lock两种功能.
		- 每次取放对象,先用random随机数获取Entry数组下标,再trylock(),trylock()不成功就直接返回.
		- 这样好像利用率不高...不过加锁粒度小,锁冲突概率也小