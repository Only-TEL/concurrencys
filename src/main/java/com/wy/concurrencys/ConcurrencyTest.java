package com.wy.concurrencys;


import com.wy.concurrencys.annotations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 测试线程安全的类
 */
@NotThreadSafe
@Slf4j
public class ConcurrencyTest {

    // 请求总数
    private static int clientTotal = 1000;
    // 每次请求的线程数
    private static int threadTotal = 50;
    // 计数器
    private static int count = 0;

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
                    log.error("线程操作错误",ex);
                }
                countDownLatch.countDown();
            });
        }
        try{
            countDownLatch.await();
        }catch (Exception e){
            log.error("计数器释放错误",e);
        }
        // 关闭线程池
        executorService.shutdown();
        // 很少出现1000，原因在于add方法中的操作不是一个原子操作，线程不安全
        log.info("\ncount={}",count);

    }
    public static void add(){
        count++;
    }
}
