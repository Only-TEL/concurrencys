package com.wy.concurrencys.example.threadlocal;

public class RequestHolder {

    /**
     * ThreadLocal是提供线程内部的局部变量，只存在线程的生命周期中
     * 一个线程中的ThreadLocalMap可以有多个key
     */
    private final static ThreadLocal<Long> requestHolder = new ThreadLocal<>();

    // set方法：获取当前线程的threadLocalMap
    // 以ThreadLocal(requestHolder)为key，设置value
    public static void add(Long id){
        requestHolder.set(id);
    }
    public static Long getId(){
        return requestHolder.get();
    }
    // 移除数据防止内存泄漏
    public static void remove(){
        requestHolder.remove();
    }
}
