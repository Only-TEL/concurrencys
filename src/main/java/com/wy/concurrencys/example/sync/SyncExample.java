package com.wy.concurrencys.example.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 当一个方法内部只有synchronized代码块时，等同与将synchronized放在方法声明上
 * synchronized关键字在子类不重写该方法时会被继承
 * 如果子类重写了该方法，如需同步则需要加synchronized关键字，不然不会同步执行
 */
@Slf4j
public class SyncExample {

    // 修饰代码块
    public void test1(int j){
        synchronized(this){
            for(int i=0;i<10;i++){
                log.info("test1-->i={},j={}",i,j);
            }
        }
    }
    // 修饰一个方法
    public synchronized void test2(int j){
        for(int i=0;i<10;i++){
            log.info("test2-->i={},j={}",i,j);
        }
    }

    public static void main(String[] args) {
        SyncExample syncExample1 = new SyncExample();
        SyncExample syncExample2 = new SyncExample();
        ExecutorService executorService = new ThreadPoolExecutor(2,4,120, TimeUnit.SECONDS, new ArrayBlockingQueue(2));
        executorService.execute(()->{
            syncExample1.test1(1);
            //syncExample1.test2(1);
        });
        executorService.execute(()->{
            syncExample1.test1(2);
            //syncExample2.test1(2);
            //syncExample2.test2(2);
        });
    }

}
class SyncExample1 extends SyncExample{

    public static void main(String[] args) {
        SyncExample1 syncExample1 = new SyncExample1();

        new Thread(new Runnable() {
            @Override
            public void run() {
                syncExample1.test1(1);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                syncExample1.test1(2);
            }
        }).start();

    }
}
