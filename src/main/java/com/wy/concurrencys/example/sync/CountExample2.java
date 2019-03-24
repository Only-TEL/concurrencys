package com.wy.concurrencys.example.sync;


import com.wy.concurrencys.annotations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@ThreadSafe
@Slf4j
public class CountExample2 {

    // 请求总数
    private static int clientTotal = 1000;
    // 每次请求的线程数
    private static int threadTotal = 50;
    // 计数器使用AtomicInteger
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
    public synchronized static void add(){
        count++;
    }
}
