package com.wy.concurrencys.example.immutable;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
@Slf4j
public class ImmutableExample1 {

    private final static Integer a = 1;
    private final static String b = "2";
    /**
     * 被final修饰的引用类型的地址指向不允许改变，但内部数据可以改变
     */
    private final static Map<Integer,Integer> map = Maps.newHashMap();

    static{
        map.put(1,2);
        map.put(3,4);
        map.put(5,6);
    }
    private void test(final int param){
        // final修饰参数，保证参数在函数内部执行的不变性
        // param = 1;
    }

    public static void main(String[] args) {
//        a = 3;                    // 编译错误
//        b = "S";                  // 编译错误
//        map = new HashMap<>();    // 编译错误
        map.put(8,9);
        log.info("map:key=1,value={}",map.get(1));
    }
}
