package com.wy.concurrencys.example.count;


import com.wy.concurrencys.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.LongAdder;

/**
 * Atomic类在底层是基于CAS实现的，CAS操作在一个死循环内不断的修改目标值，直到修改成功
 * 在线程竞争很小的情况下，很容易会修改成功；但在线程竞争激烈的情况下，容易多次修改不成功，会影响到性能
 * LongAdder
 *  优点：内部会有一个Base存储着value的偏移量在并发较低的情况下可以直接对base的值进行CAS操作
 *      LongAdder内部维护一个cells数组，在高并发的情况下，每一个线程会hash出一个随机值
 *      根据这个hash值可以对cells数组进行+1，-1等操作。这样在高并发的环境下，减小了因为CAS操作带来的压力
 *      需要获取这个值时，通过将cell数组中的值与base相加就可以拿到最终的结果。
 *  缺点：并发很高的时候下会出现统计误差
 */
@ThreadSafe
public class AtomicExample3 {

    private static Logger logger = LoggerFactory.getLogger(AtomicExample3.class);
    // 请求总数
    private static int clientTotal = 1000;
    // 每次请求的线程数
    private static int threadTotal = 50;
    // 计数器使用与AtomicInteger很相似的LongAdder
    private static LongAdder count = new LongAdder();

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
        // 1000
        logger.info("\ncount="+count);

    }
    public static void add(){
        count.increment();
    }
}
