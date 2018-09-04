package com.yioa.ssm.util;

/**
 * Created by tao on 2017-05-28.
 */
public enum CarFlows {

    CAR_FLOW_AUDIT("派单审核", "已派单"),            // 待审批
    CAR_FLOW_DRIVER_COMPLETE("驾驶员回单", "待驾驶员回单"),  // 回单
    CAR_FLOW_COMPLETE("待回单", "驾驶员已回单"),          // 回单
    FLOW_DONE("已归档","已归档");                    // 结束


    private String name;

    private String status;

    private CarFlows(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

}
