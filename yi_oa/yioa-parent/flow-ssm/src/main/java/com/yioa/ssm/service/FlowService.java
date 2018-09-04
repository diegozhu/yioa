package com.yioa.ssm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xseed.ssmflow.cfg.service.FlowPersist;
import com.yioa.common.exception.YiException;
import com.yioa.common.util.SysErrorCnst;
import com.yioa.ssm.domain.FlowCustParam;
import com.yioa.ssm.domain.FlowHandlerVo;
import com.yioa.ssm.domain.FlowInfoVo;
import com.yioa.ssm.util.OaEvents;

/**
 * 流程相关的系列服务
 * Created by tao on 2017-05-30.
 */
@Service
public class FlowService {

    @Autowired
    private FlowPersist flowPersist;

//    private FlowCustParam makeFlowCustParam(WorkOrderVo workOrderVo, String handleBy, String handleById) {
//        FlowCustParam flowCustParam = new FlowCustParam();
//        flowCustParam.setWorkOrderId(workOrderVo.getWorkOrderId());
//        flowCustParam.setHandleBy(handleBy);
//        flowCustParam.setHandleById(handleById);
//        return flowCustParam;
//    }

    public String flowSubmit(String userId, String userName, String orderId, String currentFlowStage, String event, FlowHandlerVo flowHandlerVo, FlowInfoVo flowInfoVo, String orderType) throws YiException {

        //先走流程，再改工单状态
    	
        FlowCustParam flowCustParam = new FlowCustParam(orderId,
                userName,
                userId,
                flowInfoVo.getFlowInfoId(), flowHandlerVo, orderType);
        flowCustParam.setNotes(flowInfoVo.getNotes());
        flowCustParam.setAttachFile(flowInfoVo.getAttachFile());
        flowCustParam.setPreFlowInfoId(flowInfoVo.getFlowInfoId());

        boolean suc = this.subFlow(flowCustParam, currentFlowStage, event);

        if (!suc) {
            throw new YiException(SysErrorCnst.FLOW_RUN_ERR, SysErrorCnst.MSG_FLOW_RUN_ERR);
        }
        return "1";
    }


    public boolean subFlow(FlowCustParam flowCustParam, String currentflowStage, String event) {

        return flowPersist.change(flowCustParam, event, currentflowStage);

    }


    @Deprecated
    public boolean startFlow(FlowCustParam flowCustParam, String currentflowStage) {

        return flowPersist.change(flowCustParam, OaEvents.SUBMIT.name(), currentflowStage);
    }

    @Deprecated
    public boolean auditNo(FlowCustParam flowCustParam, String currentflowStage) {

        return flowPersist.change(flowCustParam, OaEvents.AUDIT_NO.name(), currentflowStage);
    }

    @Deprecated
    public boolean auditPass(FlowCustParam flowCustParam, String currentflowStage) {

        return flowPersist.change(flowCustParam, OaEvents.AUDIT_PASS.name(), currentflowStage);
    }

    @Deprecated

    public boolean milestone(FlowCustParam flowCustParam, String currentflowStage) {

        return flowPersist.change(flowCustParam, OaEvents.MILESTONE.name(), currentflowStage);
    }


    @Deprecated
    public boolean complete(FlowCustParam flowCustParam, String currentflowStage) {

        return flowPersist.change(flowCustParam, OaEvents.COMPLETE.name(), currentflowStage);
    }

}
