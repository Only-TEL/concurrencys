package com.wy.concurrencys.example.unsafeclass;

import com.wy.concurrencys.annotations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.concurrent.*;

@NotThreadSafe
@Slf4j
public class HashSetExample {

    // 请求总数
    public static int clientTotal = 5000;

    // 同时并发执行的线程数
    public static int threadTotal = 200;

    private static HashSet<Integer> set = new HashSet<>();

    public static void main(String[] args) throws Exception{

        ExecutorService executorService = new ThreadPoolExecutor(threadTotal,
                threadTotal*2,30*60, TimeUnit.SECONDS,new ArrayBlockingQueue(threadTotal));
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            final int count = i;
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    update(count);
                    semaphore.release();
                } catch (Exception e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        // 如果线程安全的话，理论上 set.size == clientTotal
        // 最后输出结果不为总产长度
        log.info("size:{}", set.size());
    }

    public static void update(int i){
        set.add(i);

    }
}
