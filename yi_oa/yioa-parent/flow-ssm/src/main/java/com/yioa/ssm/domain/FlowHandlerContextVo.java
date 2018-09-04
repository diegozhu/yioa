package com.yioa.ssm.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 本类用以存储执行人的上下文，预备使用spel来执行，确认某环节的执行人
 * Created by tao on 2017-06-01.
 */
//FIXME 未完成

public class FlowHandlerContextVo {

    private String workOrderId;

    public Map<String,FlowInfoVo> flowInfoVoMap =  new HashMap<>();




    public FlowHandlerContextVo(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public void addFlowInfoVo(String flowStage ,FlowInfoVo vo){
        this.flowInfoVoMap.put(flowStage, vo);

    }

    public FlowInfoVo getFlowInfoVoByFlowStage(String flowStage){

        return this.flowInfoVoMap.get(flowStage);
    }


}

