package com.wy.concurrencys.example.singleton;

import com.wy.concurrencys.annotations.NotThreadSafe;

/**
 * 单例的实现1 懒汉式
 */
@NotThreadSafe
public class SingletonExample1 {

    private SingletonExample1(){

    }
    private static SingletonExample1 instance = null;

    public static SingletonExample1 getInstance(){
        if(instance == null){
            instance = new SingletonExample1();
        }
        return instance;
    }
}
