package com.wy.concurrencys.example.jucclass;

import com.wy.concurrencys.annotations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.concurrent.*;

/**
 * CopyOnWriteArrayList使用了一种写时复制的方法。
 * 当有新元素添加到CopyOnWriteArrayList时，先从原有的数组中拷贝一份出来
 * 然后在新的数组做写操作，写完之后，再将原来的数组引用指向到新数组。
 * 读取操作并为加锁：
 *  1)写操作未完成时，读取旧数据
 *  2)写操作完成但没有指向新数组时，读取旧数据
 *  3)写操作完成并指向了新数组时，读取新数据
 * add remove contains方法是线程安全的
 * addAll removeAll containAll 多次调用add remove contains不能保证整个add过程是原子性的
 * 需要添加额外的同步操作
 * other:
 *  支持迭代器操作，支持迭代，foreach的remove操作
 */
@Slf4j
@ThreadSafe
public class CopyOnWriteArrayListExample {
    // 请求总数
    public static int clientTotal = 5000;

    // 同时并发执行的线程数
    public static int threadTotal = 200;

    private static CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();

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
        // 如果线程安全的话，理论上 list.size == clientTotal
        // 最后输出结果不为总产长度
        log.info("size:{}", list.size());

        Iterator<Integer> it = list.iterator();
        while(it.hasNext()){
            Integer i = it.next();
            if(i.equals(2)){
                list.remove(i);
                log.info("remove 2 success");
                log.info("size:{}", list.size());
            }
        }
        for (Integer i:list){
            if(i.equals(10)){
                list.remove(i);
                log.info("remove 10 success");
                log.info("size:{}", list.size());
            }
        }
    }

    public static void update(int i){
        list.add(i);

    }

}
