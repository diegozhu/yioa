package com.xseed.xoauth.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by tao on 2017-05-09.
 */
public class Tmain {

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.print(URLEncoder.encode("http://192.144.133.55/a/hi","UTF-8"));
//        System.out.print(String.valueOf(null));
    }

}
