package com.wy.concurrencys.example.count;


import com.wy.concurrencys.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

@ThreadSafe
public class AtomicExample2 {

    private static Logger logger = LoggerFactory.getLogger(AtomicExample2.class);
    // 请求总数
    private static int clientTotal = 1000;
    // 每次请求的线程数
    private static int threadTotal = 50;
    // 计数器使用AtomicLong
    private static AtomicLong count = new AtomicLong(0);

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
                    logger.error("线程操作错误");
                }
                countDownLatch.countDown();
            });
        }
        try{
            countDownLatch.await();
        }catch (Exception e){
            logger.error("计数器释放错误");
        }
        // 关闭线程池
        executorService.shutdown();
        /**
         * 一直是1000与AtomicInteger结果相同
         */
        logger.info("\ncount="+count);

    }
    public static void add(){
        // 先get在自增
        count.getAndIncrement();
        // 先自增在get
        // count.incrementAndGet();
    }
}
