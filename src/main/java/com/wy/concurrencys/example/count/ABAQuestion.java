package com.wy.concurrencys.example.count;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 尽管有可能值没有变化，但是地址已经改变了
 * 解决方法：AtomicStampedReference
 *  每次更新会带上一个版本号
 */
public class ABAQuestion {

    private static Logger logger = LoggerFactory.getLogger(ABAQuestion.class);
    private static AtomicInteger atomicInteger = new AtomicInteger(100);

    public static void main(String[] args) throws Exception{
        Thread intT1 = new Thread(new Runnable() {
            @Override
            public void run() {
                atomicInteger.compareAndSet(100,101);
                logger.info("thread intT1 value="+atomicInteger.get());
                atomicInteger.compareAndSet(101,100);
                logger.info("thread intT1 value="+atomicInteger.get());
            }
        });

        Thread intT2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    TimeUnit.SECONDS.sleep(1);
                }catch (InterruptedException ie){
                    ie.printStackTrace();
                }
                boolean flag = atomicInteger.compareAndSet(100,101);
                logger.info("thread intT2 value="+atomicInteger.get()+",thread 2 update is"+flag);
            }
        });

        intT1.start();
        intT2.start();
    }

}
