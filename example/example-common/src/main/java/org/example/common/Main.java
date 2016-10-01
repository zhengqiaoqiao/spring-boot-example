package org.example.common;

import org.example.common.date.DateUtil;


/**
 * Created by zheng.qq on 2016/8/26.
 */
public class Main {
    public static void main(String[] args){
        String time = DateUtil.getNow();
        System.out.println(time);
    }
}
