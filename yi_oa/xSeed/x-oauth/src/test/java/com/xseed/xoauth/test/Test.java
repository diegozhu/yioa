package com.xseed.xoauth.test;

import java.security.SecureRandom;

/**
 * Created by tao on 2017-05-21.
 */
public class Test {
    public static void main(String[] args) {
        SecureRandom result = new SecureRandom();
        System.out.println(result.nextInt());
        System.out.println(result.nextInt());
    }
}
