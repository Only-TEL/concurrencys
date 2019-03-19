package com.wy.concurrencys.example.count;

import com.wy.concurrencys.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

@ThreadSafe
public class AtomicFieldUpdaterT {

    private volatile int count = 100;

    public static AtomicIntegerFieldUpdater<AtomicFieldUpdaterT> updater = AtomicIntegerFieldUpdater.newUpdater(AtomicFieldUpdaterT.class,"count");

    public static void main(String[] args) {
        AtomicFieldUpdaterT fieldUpdaterT = new AtomicFieldUpdaterT();
        if(updater.compareAndSet(fieldUpdaterT,100,120)){
            System.out.println("update success 1!! count="+fieldUpdaterT.getCount());
        }
        if(updater.compareAndSet(fieldUpdaterT,100,120)){
            System.out.println("update success 2!! count="+fieldUpdaterT.getCount());
        }else{
            System.out.println("update failed!! count="+fieldUpdaterT.getCount());
        }
    }

    public int getCount(){
        return count;
    }

}
