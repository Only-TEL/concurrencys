package com.wy.concurrencys.example.count;

import com.wy.concurrencys.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * AtomicReference只是提供了少量的原子操作：compareAndSet getAndSet
 * 都是对AtomicReference内部引用进行原子操作
 * 并不能保证对内部引用属性的原子操作
 * 如果需要实现--> AtomicReferenceFieldUpdater<T,V>
 *      T - 持有可更新字段的对象的类型
 *      V - 字段的类型
 */
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
