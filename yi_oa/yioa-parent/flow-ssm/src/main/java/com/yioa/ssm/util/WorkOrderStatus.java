package com.yioa.ssm.util;

/**
 * Created by tao on 2017-06-03.
 */
public enum WorkOrderStatus {
//    已派单：定单已经派出，主办人还没有接单。
//    处理中：主办人已接单
//    阶段回单：主办人已阶段回单
//    已回单：主办人已回单
//    已归档：派单人已经回单评审后，流程结束


    STATUS_SUBMITTED("已派单"),                 // 已派单
    STATUS_RECEIVED("处理中"),    // 处理中
    STATUS_MILESTONE("阶段回单"),    // 阶段回单
    STATUS_COMPLETE("已回单"),            // 已回单
    STATUS_DONE("已归档");                    // 已归档


    private String name;

    private WorkOrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
