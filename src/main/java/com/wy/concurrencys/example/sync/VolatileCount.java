package com.wy.concurrencys.example.sync;


import com.wy.concurrencys.annotations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * volatile使用:注意语句1与语句2的重排序会导致doSomethingConfig出错
 * volatile boolean inited = false;
 * 线程1
 *  context = loadContext(); //1
 *  inited = true;           //2
 * 线程2
 * while(!inited){
 *     sleep();
 * }
 * doSomethingConfig(context);
 */
@NotThreadSafe
@Slf4j
public class VolatileCount {

    // 请求总数
    private static int clientTotal = 1000;
    // 每次请求的线程数
    private static int threadTotal = 50;
    // 计数器使用AtomicInteger
    private volatile static int count = 0;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for(int i=0;i<clientTotal;i++){
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                }catch (Exception ex){
                    log.error("线程操作错误");
                }
                countDownLatch.countDown();
            });
        }
        try{
            countDownLatch.await();
        }catch (Exception e){
            log.error("计数器释放错误");
        }
        // 关闭线程池
        executorService.shutdown();

        log.info("\ncount={}",count);

    }
    public static void add(){
        count++;
        // 1.count  读
        // 2.+1     改
        // 3.count  写
    }
}
