package com.wy.concurrencys.example.singleton;

import com.wy.concurrencys.annotations.Recommend;
import com.wy.concurrencys.annotations.ThreadSafe;

/**
 * 单例的实现 枚举类
 */
@ThreadSafe
@Recommend
public class SingletonExample7 {

    private SingletonExample7(){

    }
    public static SingletonExample7 getInstance(){
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton{
        INSTANCE;
        private SingletonExample7 singleton;
        // 构造函数，JVM执行，只执行一次
        Singleton(){
            singleton = new SingletonExample7();
        }
        public SingletonExample7 getInstance(){
            return singleton;
        }

    }
}
