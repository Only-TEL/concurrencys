package com.wy.concurrencys.example.count;

import com.wy.concurrencys.annotations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

@ThreadSafe
@Slf4j
public class AtomicFieldUpdaterT {

    private volatile int count = 100;

    public static AtomicIntegerFieldUpdater<AtomicFieldUpdaterT> updater = AtomicIntegerFieldUpdater.newUpdater(AtomicFieldUpdaterT.class,"count");

    public static void main(String[] args) {
        AtomicFieldUpdaterT fieldUpdaterT = new AtomicFieldUpdaterT();
        if(updater.compareAndSet(fieldUpdaterT,100,120)){
            log.info("update success 1!! count={}",fieldUpdaterT.getCount());
        }
        if(updater.compareAndSet(fieldUpdaterT,100,120)){
            log.info("update success 2!! count={}",fieldUpdaterT.getCount());
        }else{
            log.info("update failed!! count={}",fieldUpdaterT.getCount());
        }
    }

    public int getCount(){
        return count;
    }

}
