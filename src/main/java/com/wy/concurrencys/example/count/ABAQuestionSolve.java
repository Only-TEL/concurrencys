package com.wy.concurrencys.example.count;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 使用了AtomicStampedReference后，每一次更新需要指定版本号，就不会造成地址问题
 */
@Slf4j
public class ABAQuestionSolve {

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
                log.info("thread ref1:value={}" , atomicStampedReference.getReference());
                atomicStampedReference.compareAndSet(101, 100,
                        atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
                log.info("thread ref1:value={}" , atomicStampedReference.getReference());
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
                log.info("after sleep --> stamp = {}",atomicStampedReference.getStamp());
                boolean flag = atomicStampedReference.compareAndSet(100,101,stamp,stamp+1);
                log.info("thread ref2:{},update is {}",atomicStampedReference.getReference(),flag);
            }
        });

        ref1.start();
        ref2.start();
    }

}
