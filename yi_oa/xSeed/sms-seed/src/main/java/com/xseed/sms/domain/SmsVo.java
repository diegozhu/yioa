package com.xseed.sms.domain;

/**
 * Created by tao on 2017-05-03.
 */
public class SmsVo {


    private String smsParamString;

    private String recNum;

    public SmsVo(String smsParamString, String recNum) {
        this.smsParamString = smsParamString;
        this.recNum = recNum;
    }


    public String getSmsParamString() {
        return smsParamString;
    }

    public void setSmsParamString(String smsParamString) {
        this.smsParamString = smsParamString;
    }

    public String getRecNum() {
        return recNum;
    }

    public void setRecNum(String recNum) {
        this.recNum = recNum;
    }


    @Override
    public String toString() {
        return "SmsVo{" +
                "smsParamString='" + smsParamString + '\'' +
                ", recNum='" + recNum + '\'' +
                '}';
    }
}
