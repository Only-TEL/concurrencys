package com.wy.concurrencys.example.publish;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
@Slf4j
public class UnsafePublish {

    private String[] state = {"A","B","C"};

    public String[] getState(){
        return state;
    }

    public static void main(String[] args) {

        UnsafePublish unsafePublish = new UnsafePublish();
        log.info("state=> {}", Arrays.toString(unsafePublish.getState()));
        // 不安全的发布state内部的对象
        unsafePublish.getState()[0] = "D";
        log.info("state=> {}", Arrays.toString(unsafePublish.getState()));
    }

}
