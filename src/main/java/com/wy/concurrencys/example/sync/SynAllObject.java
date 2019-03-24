package com.wy.concurrencys.example.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
@Slf4j
public class SynAllObject {

    // 修饰代码块
    public  void test1(int j){
        synchronized(SynAllObject.class){
            for(int i=0;i<10;i++){
                log.info("test1-->i={},j={}",i,j);
            }
        }
    }
    // 修饰一个方法
    public static synchronized void test2(int j){
        for(int i=0;i<10;i++){
            log.info("test2-->i={},j={}",i,j);
        }
    }

    public static void main(String[] args) {
        SynAllObject syn1 = new SynAllObject();
        SynAllObject syn2 = new SynAllObject();
        ExecutorService executorService = new ThreadPoolExecutor(2,4,120, TimeUnit.SECONDS, new ArrayBlockingQueue(2));
        executorService.execute(()->{
            syn1.test1(1);
            //syncExample1.test2(1);
        });
        executorService.execute(()->{
            syn2.test1(2);
            //syncExample2.test1(2);
            //syncExample2.test2(2);
        });
    }

}
