package com.xseed.sms.domain;

/**
 *
 * 验证码存储对象
 * Created by tao on 2017-05-04.
 *
 */
public class VerifyCodeVo {

    private String sessionId;

    private long times;

    private String num;

    public VerifyCodeVo(String sessionId, long times, String num) {
        this.sessionId = sessionId;
        this.times = times;
        this.num = num;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "VerifyCodeVo{" +
                "sessionId='" + sessionId + '\'' +
                ", times=" + times +
                ", num='" + num + '\'' +
                '}';
    }
}
