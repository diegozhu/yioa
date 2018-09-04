package com.yioa.ssm.util;

/**
 * Created by liangpengcheng on 2018/4/9.
 */
public enum PanelFlows {
    PANEL_FLOW_AUDIT("派单审核", "已派单"),                 // 启动
    PANEL_FLOW_COMPLETE("待回单", "已审核"),    // 回单
    FLOW_DONE("已归档","已归档");
    private String name;
    private String status;
    private PanelFlows(String name,String status){
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
