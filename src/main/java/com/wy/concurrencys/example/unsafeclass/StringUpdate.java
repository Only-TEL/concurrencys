package com.wy.concurrencys.example.unsafeclass;

import com.wy.concurrencys.annotations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
@NotThreadSafe
@Slf4j
public class StringUpdate {

    // 请求总数
    private static int clientTotal = 5000;
    // 每次请求的线程数
    private static int threadTotal = 50;
    // StringBuffer
    private static StringBuilder builder = new StringBuilder();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for(int i=0;i<clientTotal;i++){
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    update();
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
        log.info("size={}",builder.length());
    }
    public static void update(){
        builder.append("A");
    }
}
