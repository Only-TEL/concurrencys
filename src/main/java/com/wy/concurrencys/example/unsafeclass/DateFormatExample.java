package com.wy.concurrencys.example.unsafeclass;

import com.wy.concurrencys.annotations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
@NotThreadSafe
public class DateFormatExample {

    // 请求总数
    private static int clientTotal = 5000;
    // 每次请求的线程数
    private static int threadTotal = 50;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    //使用threadLocal来保证SimpleDateFormat的安全性
    private static ThreadLocal<DateFormat> dateFormatHolder = new ThreadLocal(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

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
    }
    public static void update(){
        try{
            // SimpleDateFormat在多线程下共享使用就会出现线程不安全情况
            simpleDateFormat.format("20190303");
        }catch(Exception e){
            log.error("parse error");
        }
    }
}
