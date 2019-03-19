package com.wy.concurrencys.example.count;

import com.wy.concurrencys.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;
@ThreadSafe
public class AtomicRefenceT {

    public static AtomicReference<Integer> ar = new AtomicReference<>(0);

    public static void main(String[] args) {
        ar.compareAndSet(0,2);
        ar.compareAndSet(0,1);
        ar.compareAndSet(1,3);
        ar.compareAndSet(2,4);
        ar.compareAndSet(3,5);
        System.out.println("count="+ar.get());
    }

}
