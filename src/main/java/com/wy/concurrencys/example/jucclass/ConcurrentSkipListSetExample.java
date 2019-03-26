package com.wy.concurrencys.example.jucclass;

import com.wy.concurrencys.annotations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * SkipList跳表
 *
 *
 * ConcurrentSkipListSet
 *  1)支持自然排序
 *  2）基于ConcurrentSkipListMap实现
 *  3）不允许使用null元素，区分null元素返回和不存在的元素
 *  4）addAll、removeAll、retainAll线程不安全
 */
@Slf4j
@ThreadSafe
public class ConcurrentSkipListSetExample {

    // 请求总数
    public static int clientTotal = 5000;

    // 同时并发执行的线程数
    public static int threadTotal = 200;

    private static ConcurrentSkipListSet<Integer> set = new ConcurrentSkipListSet<>();

    public static void main(String[] args) throws Exception{

        ExecutorService executorService = new ThreadPoolExecutor(threadTotal,
                threadTotal*2,30*60, TimeUnit.SECONDS,new LinkedBlockingQueue<>());
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
        // 最后输出结果为总长度
        log.info("size:{}", set.size());
    }

    public static void update(int i){
        set.add(i);

    }



}
