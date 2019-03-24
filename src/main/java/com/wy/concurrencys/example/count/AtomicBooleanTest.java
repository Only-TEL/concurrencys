package com.wy.concurrencys.example.count;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 如何让一段代码只执行一次AtomicBoolean
 */
@Slf4j
public class AtomicBooleanTest {

    private static AtomicBoolean isHappened = new AtomicBoolean(false);
    // 请求总数
    private static int clientTotal = 1000;
    // 每次请求的线程数
    private static int threadTotal = 50;

    public static void main(String[] args) {
        ExecutorService executorService =
                new ThreadPoolExecutor(threadTotal,
                        threadTotal*2,
                        60,TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>());
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for(int i=0;i<clientTotal;i++){
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    test();
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
    }
    private static void test(){
        if(isHappened.compareAndSet(false,true)){
            log.info("execute");
        }
    }

}
