package com.yioa.ssm.domain;

import com.yioa.ssm.domain.FlowHandlerVo;

/**
 *
 * 流程流转的时候，传递的额外参数
 *
 * Created by tao on 2017-05-30.
 */
public class FlowCustParam {

    private String workOrderId;

    private String handleBy;

    private String handleById;

    private String preFlowInfoId;

    //派单的时候，指定处理人为某某
    private FlowHandlerVo flowHandlerVo;


    //回单内容
    private String notes;

    //附件信息
    private String attachFile;


    //工单类型，好走不同的处理
    private String orderType;

    public FlowCustParam() {
    }

    public FlowCustParam(String workOrderId, String handleBy, String handleById, String preFlowInfoId,FlowHandlerVo flowHandlerVo,String orderType) {
        this.workOrderId = workOrderId;
        this.handleBy = handleBy;
        this.handleById = handleById;
        this.preFlowInfoId = preFlowInfoId;
        this.flowHandlerVo = flowHandlerVo;
        this.orderType = orderType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(String attachFile) {
        this.attachFile = attachFile;
    }

    public FlowHandlerVo getFlowHandlerVo() {
        return flowHandlerVo;
    }

    public void setFlowHandlerVo(FlowHandlerVo flowHandlerVo) {
        this.flowHandlerVo = flowHandlerVo;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getPreFlowInfoId() {
        return preFlowInfoId;
    }

    public void setPreFlowInfoId(String preFlowInfoId) {
        this.preFlowInfoId = preFlowInfoId;
    }

    public String getHandleBy() {
        return handleBy;
    }

    public void setHandleBy(String handleBy) {
        this.handleBy = handleBy;
    }

    public String getHandleById() {
        return handleById;
    }

    public void setHandleById(String handleById) {
        this.handleById = handleById;
    }
}
