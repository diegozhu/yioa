package com.xseed.sms.common;

/**
 * 短信事件
 * Created by tao on 2017-08-19.
 */
public class SmsEvent {
    private String num;
    private String msg;

    public SmsEvent(String num, String msg) {
        this.num = num;
        this.msg = msg;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "SmsEvent{" +
                "num='" + num + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
