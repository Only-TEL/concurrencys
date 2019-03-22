package com.wy.concurrencys.example.singleton;

import com.wy.concurrencys.annotations.ThreadSafe;

/**
 * 单例的实现 懒汉式
 * 静态变量需要写在静态代码块之前，不然会出现空指针异常
 */
@ThreadSafe
public class SingletonExample6 {

    private SingletonExample6(){

    }
    private static SingletonExample6 instance = null;

    static{
        instance = new SingletonExample6();
    }

    // private static SingletonExample6 instance = null; 空指针异常

    public static SingletonExample6 getInstance(){
        return instance;
    }

    public static void main(String[] args) {
        System.out.println(SingletonExample6.getInstance().hashCode());
        System.out.println(SingletonExample6.getInstance().hashCode());
    }
}
