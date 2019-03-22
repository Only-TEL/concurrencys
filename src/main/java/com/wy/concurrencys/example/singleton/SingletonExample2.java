package com.wy.concurrencys.example.singleton;

import com.wy.concurrencys.annotations.ThreadSafe;

/**
 * 单例的实现2 饿汉式
 * 注意限制构造函数
 */
@ThreadSafe
public class SingletonExample2 {

    private SingletonExample2(){

    }
    private static SingletonExample2 instance = new SingletonExample2();

    public static SingletonExample2 getInstance(){
        return instance;
    }
}
