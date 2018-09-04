package com.yioa.ssm.util;

/**
 * Created by tao on 2017-06-03.
 */
public enum CarSendOrderStatus {



    STATUS_SUBMITTED("已派单"),                 // 已派单
    STATUS_RECEIVED("处理中"),    // 处理中
//    STATUS_COMPLETE("已回单"),            // 已回单
    STATUS_DONE("已归档");                    // 已归档


    private String name;

    private CarSendOrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
