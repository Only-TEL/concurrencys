package com.wy.concurrencys.example.unsafeclass;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Vector;

/**
 * 集合元素的删除
 * 在使用foreach或者iterator进行集合的遍历时
 * 尽量不要在遍历过程中进行remove等相关的更新操作
 * 需要此类操作时，需要记下需要操作的index
 * 遍历结束在进行更新操作
 */
@Slf4j
public class RemoveElement {

    // foreach java.util.ConcurrentModificationException
    private static void remove1(Vector<Integer> vector){
        for (Integer i : vector){
            if(i.equals(3)){
                vector.remove(i);
                log.info("remove success");
            }
        }
    }
    // Iterator java.util.ConcurrentModificationException
    private static void remove2(Vector<Integer> vector){
        Iterator<Integer> iterator = vector.iterator();
        while(iterator.hasNext()){
            Integer i = iterator.next();
            if(i.equals(3)){
                vector.remove(i);
                log.info("remove success");
            }
        }
    }

    // for
    private static void remove3(Vector<Integer> vector){
        for (int i=0;i<vector.size();i++){
            if(vector.get(i).equals(3)){
                vector.remove(i);
                log.info("remove success");
            }
        }
    }

    public static void main(String[] args) {
        Vector<Integer> vector = new Vector<>(5);
        vector.add(2);
        vector.add(3);
        vector.add(6);
        vector.add(9);
//        remove1(vector);
//        remove2(vector);
        remove3(vector);
    }
}
