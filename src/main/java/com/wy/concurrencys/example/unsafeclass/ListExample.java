package com.wy.concurrencys.example.unsafeclass;

import com.wy.concurrencys.annotations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * list.add() -> elementData[size++] = e;
 * set.add()  -> hashMap.put(key,final object)
 * map.put()  -> hashMap.put(key,value)
 *            -> 检查Map是否为空；检查key的hashcode对应位置上是否存在值；
 *               查找需要插入的节点位置进行put数据。
 *               这三种情况在多线程的情况下并不会依序执行，造成put数据减少或者错误
 * log.info("ft={}",(false || 56<58?56:58));
 * log.info("ft={}",(true && 56<58?56:58));
 * 都会抛出最后一个的结果
 */
@NotThreadSafe
@Slf4j
public class ListExample {

    // 请求总数
    public static int clientTotal = 5000;

    // 同时并发执行的线程数
    public static int threadTotal = 200;

    private static List<Integer> list = new ArrayList<>();

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
        // 如果线程安全的话，理论上 list.size == clientTotal
        // 最后输出结果不为总产长度
        log.info("size:{}", list.size());
    }

    public static void update(int i){
        list.add(i);

    }
}
