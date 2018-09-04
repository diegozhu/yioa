package com.yioa.common.util;

/**
 * Created by tao on 2017-05-22.
 */
public final class CommonCnst {

    public static final  String USER_ID = "sys_user_id";
    public static final  String USER_LOGIN_NAME = "sys_user_login_name";
    public static final  String USER_INFO = "sys_user_info";

    /**
     * http 状态码
     * ajax 请求，当session失效时，返回999，前段处理下进行重登陆
     *
     */
    public static final int AJAX_LOGIN_HTTP_CODE = 999;

    /**
     * http 状态码
     * ajax 请求，当session失效时，返回999，前段处理下进行重登陆
     *
     */
    public static final int AJAX_ERROR = 500;


    /**
     * 已经有openid，但是没有用户，此时重定向到绑定界面
     */
    public static final int AJAX_BIND_HTTP_CODE = 998;
}
