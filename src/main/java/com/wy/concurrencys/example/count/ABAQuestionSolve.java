package com.wy.concurrencys.example.count;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 使用了AtomicStampedReference后，每一次更新需要指定版本号，就不会造成地址问题
 */
public class ABAQuestionSolve {

    private static Logger logger = LoggerFactory.getLogger(ABAQuestionSolve.class);
    private static AtomicStampedReference<Integer> atomicStampedReference =
            new AtomicStampedReference<>(100,0);

    public static void main(String[] args) {

        Thread ref1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                atomicStampedReference.compareAndSet(100, 101,
                        atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
                logger.info("thread ref1:value=" + atomicStampedReference.getReference());
                atomicStampedReference.compareAndSet(101, 100,
                        atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
                logger.info("thread ref1:value=" + atomicStampedReference.getReference());
            }
        });
        Thread ref2 = new Thread(new Runnable() {
            @Override
            public void run() {
                int stamp = atomicStampedReference.getStamp();
                try{
                    TimeUnit.SECONDS.sleep(2);
                }catch (Exception e){
                    e.printStackTrace();
                }
                logger.info("after sleep --> stamp = "+atomicStampedReference.getStamp());
                boolean flag = atomicStampedReference.compareAndSet(100,101,stamp,stamp+1);
                logger.info("thread ref2:"+atomicStampedReference.getReference()+",update is "+flag);
            }
        });

        ref1.start();
        ref2.start();
    }

}
