package com.wy.concurrencys.example.singleton;

import com.wy.concurrencys.annotations.NotRecommend;
import com.wy.concurrencys.annotations.NotThreadSafe;

/**
 * 单例的实现1 懒汉式 双重检测 线程不安全
 * instance = new SingletonExample4();
 *  1.memory=allocate() 分配对象的内存空间
 *  2.ctorInstance() 初始化对象
 *  3.instance=memory 设置instance的内存地址
 * JVM与CPU的优化，发生了指令冲排序
 *  1.memory=allocate()
 *  3.instance=memory
 *  2.ctorInstance()
 * 在重排序后会出现线程不安全的问题
 */
@NotThreadSafe
@NotRecommend
public class SingletonExample4 {

    private SingletonExample4(){

    }
    private static SingletonExample4 instance = null;

    public static SingletonExample4 getInstance(){
        // 双重检测
        if(instance == null){
            // 同步锁
            // B线程 -> 检测到instance有地址了，就会return
            // 但实际上instance对象的初始化并没有完成
            synchronized(SingletonExample4.class) {
                if(instance == null) {
                    // A线程运行到指令重排序后的第二步 已经设置了instance的地址
                    instance = new SingletonExample4();
                }
            }
        }
        return instance;
    }
}
