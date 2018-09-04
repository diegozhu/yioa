package com.xseed.sms.util;

/**
 * 将本module暴露的rest服务url提供出来
 *
 * Created by tao on 2017-05-22.
 */
public final class SmsServiceCnst {

    private SmsServiceCnst(){

    }

    public static  final String SMS_PRE = "/api/sms";

    public static  final String SMS_SENDSMS = SMS_PRE + "/send/{name}/{num}/{seq}";

    public static  final String SMS_VALIDSMS = SMS_PRE + "/verify/{code}/{num}";



}
