package com.yioa.ssm.util;

/**
 * Created by liangpengcheng on 2018/5/5.
 */
public enum MarketFlows {

    MARKET_FlOW_START("开始","开始"),                 // 启动
    MARKET_FLOW_MILESTONE("待阶段回单","已派单"),    // 阶段回单
    MARKET_FLOW_COMPLETE("待回单","已阶段回单"),    // 回单
    MARKET_FLOW_AUDIT("待审批归档","已回单"),            // 待审批
    MARKET_FLOW_DONE("已归档","已归档");                    // 结束

    private String name;

    private String status;

    private MarketFlows(String name, String status) {
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
