package com.wy.concurrencys.example.immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;

/**
 * java提供Collections工具类，提供了不允许修改集合内部元素的方法Collections.unmodifiablexx
 * 谷歌的Guava提供类似Java中的Collections：Immutablexx
 * 规范：final写在static前
 */
@Slf4j
public class ImmutableExample2 {

    private static Map<Integer,Integer> map = Maps.newHashMap();
    static{
        map.put(1,2);
        map.put(3,4);
        map.put(5,6);
        // map处理后集合内部元素就不能增删改了->会抛出异常java.lang.UnsupportedOperationException
        // unmodifiableMap unmodifiableList unmodifiableSet unmodifiableSortedSet/Map
        map = Collections.unmodifiableMap(map);
    }
    /**
     * google的Guava
     */
    private final static ImmutableList<Integer> list = ImmutableList.of(1,2,3);
    private final static ImmutableSet<Integer> set = ImmutableSet.copyOf(list);
    private final static Map<Integer,Integer> iMap = ImmutableMap.<Integer,Integer>builder()
            .put(1,2).put(3,4).put(5,6).build();

    public static void main(String[] args) {
        //iMap.put(34,54); 运行异常
        //list.add(4);     方法废弃
        //map.put(8,9);
        //map.remove(1);
        //map.replace(3,30);
        log.info("map:key=1,value={}",map.get(1));
        log.info("map:key=3,value={}",map.get(3));
        log.info("map:key=5,value={}",map.get(5));
    }
}
