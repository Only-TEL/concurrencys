##线程的一般操作
![线程操作]https://github.com/Only-TEL/concurrencys/blob/master/%E7%BA%BF%E7%A8%8B%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F.png
##线程的安全性
+ 原子性：同一个时刻，只允许一个线程访问这些操作
    #Synchronized
    + 修饰代码块-->作用调用的对象
    + 修饰成员方法-->作用调用的对象
    + 修饰一个静态方法-->作用所有对象
    + 修饰一个类-->作用所有对象
    + JAVA内存模型对于Synchronized的规定：
        1)线程解锁前，必须把共享变量的最新值刷新到主存中
        2)线程加锁时，会清空工作内存中共享变量的值，需要线程从主内存中获取最新的值
        *)加锁与解锁是同一把锁
    #ReentrantLock
+ 可见性：一个线程对主内存的修改可以被其他线程看到
    1)线程交叉执行
    2)JVM指令的重排序+线程交叉执行
    3)共享变量在线程中更新后的值没有与主存进行及时更新
    #volatile
    + 对volatile变量写操作时，会在写操作后加入一条store屏障指令，将本地内存的共享变量刷新进主存中
    * 顺序：storestore(禁止写与volatile写操作的重排序) - volatile写 - storeload(禁止volatile写与读操作的重排序)
    + 对volatile变量读操作时，会在读操作之前加入一条load屏障指令，从主存中读取共享变量
    * 顺序：volatile读 - loadload(禁止volatile读与普通读操作的重排序) - loadstore(禁止volatile读与写操作的重排序)
+ 有序性：保证线程对于共享代码的执行一定要按照我们代码的逻辑顺序执行。避免多线程下指令重排序导致结果错误
    > JAVA内存模型中,允许编译器和处理器对指令进行重排序。冲排序不会影响单线程的程序执行，但会影响多线程并发的执行正确性
    #happens-before原则
    1. 一个线程内，书写在前面的操作先于书写在后面的操作 --> 保证的单线程的有序性
    2. 一个UnLock操作先于后面对同一个锁的Lock操作    --> 锁的规则
    3. 对一个变量的写操作先与后面对这个变量的读操作    --> volatile变量
    4. 操作A先于操作B，操作B先于操作C，则操作A先于操作C --> 传递规则
    5. 线程启动规则：Thread对象的start()方法先行发生于此线程的每个一个动作
    6. 线程中断规则：对线程interrupt()方法的调用先行发生于被中断线程的代码检测到中断事件的发生
    7. 线程终结规则：线程中所有的操作都先行发生于线程的终止检测，我们可以通过Thread.join()方法结束、Thread.isAlive()的返回值手段检测到线程已经终止执行
    8. 对象终结规则：一个对象的初始化完成先行发生于他的finalize()方法的开始  
##安全发布对象的四种方式
   1.静态初始化函数中初始化一个对象的引用
   2.将对象的引用保存在volatile域或者AtomicReference对象中
   3.将对象的引用保存到某个final类型的域中
   4.将对象的引用保存到一个有锁保护的域中
##不可变对象 String
   + 对象创建之后其状态就不能修改
   + 对象所有的域都是final
   + 对象在创建期间，this引用没有溢出
##线程封闭
   - threadLocal:threadLocal在服务器携带数据
   - 数据库连接池
##线程不安全的类
   * StringBuilder与StringBuffer
   * SimpleDateFormat与JodaTime
   * ArrayList、HashSet、HashMap
##同步容器
   1. Vector、Stack
   2. HashTable(key与value都不能为null)
   3. Collections.synchronizedList(List<>)
   4. Collections.synchronizedSet(Set<>)
   5. Collections.synchronizedMap(Map<>)
   6. 集合元素的删除问题
##并发容器
   - ArrayList -> CopyOnWriteArrayList
   > CopyOnWriteArrayList在写操作时，复制原数组，如果原数组内容较多，可能导致yong gc或者full gc  
   > 不适用于实时读取的场景，适合读多写少的环境
   > 设计思想：读写分离、数据最终一致性、开辟新空间解决并发冲突
   - HashSet、TreeSet -> CopyOnWriteArraySet、ConcurrentSkipListSet
   - HashMap、TreeMap -> ConcurrentHashMap、ConcurrentSkipListMap
   > ConcurrentSkipListSet是基于ConcurrentSkipListMap实现的，内部是使用SkipList(跳表)结构实现的
   > ConcurrentSkipListMap的key有序，支持的并发比ConcurrentHashMap高，并发越高相比于ConcurrentHashMap优势越大
##安全共享策略
   1. 线程限制：一个被线程限制的对象，只允许占有它的线程修改它
   2. 共享只读：一个共享只对的对象，在没有额外同步的情况下，可以被多个线程读取，但是不能被修改
   3. 线程安全对象：在内部通过同步机制来保证线程安全，其他线程无需额外的同步就可以通过公共接口随意访问它
   4. 被守护对象：被守护对象只能通过获取特定的锁来访问
   
   
   