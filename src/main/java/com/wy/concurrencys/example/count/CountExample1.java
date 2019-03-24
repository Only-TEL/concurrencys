package com.wy.concurrencys.example.count;


import com.wy.concurrencys.annotations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Slf4j
public class CountExample1 {

    // 请求总数
    private static int clientTotal = 1000;
    // 每次请求的线程数
    private static int threadTotal = 50;
    // 计数器使用AtomicInteger
    private static AtomicInteger count = new AtomicInteger(0);

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
        /**
         * 一直是1000，原因在于add方法中的count是AtomicInteger类型
         * 它的自增操作在底层保证原子性 ---> CAS(Compare And Swap)
         */
        log.info("\ncount={}",count);

    }
    public static void add(){
        // 先get在自增
        count.getAndIncrement();
        // 先自增在get
        // count.incrementAndGet();
    }
}
