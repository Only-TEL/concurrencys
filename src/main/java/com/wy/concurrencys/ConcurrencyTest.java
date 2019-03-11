package com.wy.concurrencys;


import com.wy.concurrencys.annotations.NotThreadSafe;

import java.util.concurrent.*;

@NotThreadSafe
public class ConcurrencyTest {

    // 请求总数
    private static int clientTotal = 1000;
    // 每次请求的线程数
    private static int threadTotal = 50;
    /**
     * 计数器
     */
    private static int count = 0;

    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(
                                            threadTotal,
                                            2*threadTotal,
                                            1000,
                                            TimeUnit.SECONDS,
                                            new ArrayBlockingQueue<>(threadTotal));
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for(int i=0;i<clientTotal;i++){
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            });
        }

    }
    public static void add(){
        count++;
    }
}
