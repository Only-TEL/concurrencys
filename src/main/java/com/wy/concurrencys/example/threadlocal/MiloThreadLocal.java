package com.wy.concurrencys.example.threadlocal;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试ThreadLocal
 */
@Slf4j
public class MiloThreadLocal {

    private ThreadLocal<Long> longLocal = new ThreadLocal<>();
    private ThreadLocal<String> stringLocal = new ThreadLocal<>();

    public void set(){
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }
    public Long getId(){
        return longLocal.get();
    }
    public String getThreadName(){
        return stringLocal.get();
    }

    public static void main(String[] args) throws Exception{

        final MiloThreadLocal holder = new MiloThreadLocal();
        holder.set();
        log.info("Thread main id{=},name={}",holder.getId(),holder.getThreadName());

        Thread t1 = new Thread(() ->{
            holder.set();
            log.info("Thread t1 id={},name={}",holder.getId(),holder.getThreadName());
        });
        t1.start();
        // 需要主线程等待其他线程执行完成，在执行join后的代码
        t1.join();
        log.info("Thread main id={},name={}",holder.getId(),holder.getThreadName());
    }

}
