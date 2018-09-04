package com.yioa.ssm.util;

/**
 * Created by liangpengcheng on 2018/4/9.
 */
public enum PanelOrderStatus {

    STATUS_SUBMITTED("已派单"),                 // 已派单
    STATUS_RECEIVED("处理中"),    // 处理中
    STATUS_DONE("已归档");                    // 已归档

    private String name;
    private PanelOrderStatus(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
