package com.wy.concurrencys.example.singleton;

import com.wy.concurrencys.annotations.NotRecommend;
import com.wy.concurrencys.annotations.ThreadSafe;

/**
 * 单例的实现1 懒汉式 synchronized
 */
@ThreadSafe
@NotRecommend
public class SingletonExample3 {

    private SingletonExample3(){

    }
    private static SingletonExample3 instance = null;

    public static synchronized SingletonExample3 getInstance(){
        if(instance == null){
            instance = new SingletonExample3();
        }
        return instance;
    }
}
