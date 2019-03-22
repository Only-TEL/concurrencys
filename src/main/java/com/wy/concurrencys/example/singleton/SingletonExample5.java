package com.wy.concurrencys.example.singleton;

import com.wy.concurrencys.annotations.ThreadSafe;

/**
 * 单例的实现1 懒汉式 双重检测 线程不安全 -> 解决方法(volatile)
 */
@ThreadSafe
public class SingletonExample5 {

    private SingletonExample5(){

    }
    /**
     * volatile+双重检测 -> 禁止指令重排序
     */
    private volatile static SingletonExample5 instance = null;

    public static SingletonExample5 getInstance(){
        // 双重检测
        if(instance == null){
            // 同步锁
            synchronized(SingletonExample5.class) {
                if(instance == null) {
                    instance = new SingletonExample5();
                }
            }
        }
        return instance;
    }
}
