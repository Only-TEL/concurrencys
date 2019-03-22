package com.wy.concurrencys.example.publish;

import java.util.Arrays;

public class UnsafePublish {

    private String[] state = {"A","B","C"};

    public String[] getState(){
        return state;
    }

    public static void main(String[] args) {

        UnsafePublish unsafePublish = new UnsafePublish();
        System.out.println("state=> "+ Arrays.toString(unsafePublish.getState()));
        System.out.println("\n-------------------------------------------------\n");
        // 不安全的发布state内部的对象
        unsafePublish.getState()[0] = "D";
        System.out.println("state=> "+ Arrays.toString(unsafePublish.getState()));
    }

}
