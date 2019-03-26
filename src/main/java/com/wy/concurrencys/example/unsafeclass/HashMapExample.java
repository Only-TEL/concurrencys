package com.wy.concurrencys.example.unsafeclass;

import com.wy.concurrencys.annotations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 根据Key的HashCode进行存储，不同的key的hashcode可能相同
 * 相同hashcode的key存在同一个节点上，当这个节点的数据>=8时，节点上的链表会转换为红黑树
 * 扩容：resize() -> 保证索引不变或者索引地址偏移2次幂
 * Initializes or doubles table size. If null, allocates in accord with initial capacity target held in field threshold.
 * Otherwise, because we are using power-of-two expansion, the elements from each bin must either stay at same index,
 * or move with a power of two offset in the new table.
 *
 */
@Slf4j
@NotThreadSafe
public class HashMapExample {
    // 请求总数
    public static int clientTotal = 5000;

    // 同时并发执行的线程数
    public static int threadTotal = 200;

    private static Map<Integer,Integer> map = new HashMap<>();

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
        // 如果线程安全的话，理论上 map.size == clientTotal
        // 最后输出结果不为总产长度
        log.info("size:{}", map.size());

    }

    public static void update(int i){
        map.put(i,i*50);

    }
}
