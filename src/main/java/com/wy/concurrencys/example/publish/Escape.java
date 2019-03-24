package com.wy.concurrencys.example.publish;

import com.wy.concurrencys.annotations.NotRecommend;
import com.wy.concurrencys.annotations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

/**
 * 对象溢出：当一个对象还没有完成构造时，就使它被其他线程可见
 */
@NotThreadSafe
@NotRecommend
@Slf4j
public class Escape {

    private int thisCanBeEscape = 0;

    public Escape(){
        new InnerClass();
    }
    class InnerClass{

        public InnerClass(){
            // this引用可能溢出
            log.info("value = {}"+Escape.this.thisCanBeEscape);
        }
    }

    public static void main(String[] args) {
        new Escape();
    }
}
