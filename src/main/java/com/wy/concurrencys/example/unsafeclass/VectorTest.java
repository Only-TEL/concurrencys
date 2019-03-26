package com.wy.concurrencys.example.unsafeclass;

import com.wy.concurrencys.annotations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.Vector;

/**
 * 同步容器不一定是线程安全的
 * 出错原因：线程1和线程2都获得了相同的vector的大小
 *  存在线程1执行完remove索引为9操作时，线程2执行get索引为9的操作
 *  报ArrayIndexOutOfBoundsException
 */
@Slf4j
@NotThreadSafe
public class VectorTest {

    private static Vector<Integer> vector = new Vector<>();

    public static void main(String[] args) {

        while(true){
            for (int i=0;i<10;i++){
                vector.add(i);
            }
            Thread t1 = new Thread(){
                @Override
                public void run() {
                    for(int i=0;i<vector.size();i++){
                        // t1
                        vector.remove(i);
                    }
                }
            };
            Thread t2 = new Thread(){
                @Override
                public void run() {
                    for(int i=0;i<vector.size();i++){
                        // t2
                        vector.get(i);
                    }
                }
            };
            t1.start();
            t2.start();
        }
    }

}
