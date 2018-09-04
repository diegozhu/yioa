package com.yioa.oa.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tao on 2017-05-23.
 */
@TableName("t_work_order")
public class WorkOrderVo extends Model<WorkOrderVo>{


    @TableId("work_order_id")
    private String workOrderId;


    @TableField("work_order_code")
    private String workOrderCode;


    //主办人
    @TableField("organiser")
    private String organiser;

    //协办人
    @TableField("co_organiser")
    private String coOrganiser;

    //主办人
    @TableField("organiser_name")
    private String organiserName;

    //协办人
    @TableField("co_organiser_name")
    private String coOrganiserName;

    @TableField("work_order_type")
    private String workOrderType;

    @TableField("emergency_level")
    private String emergencyLevel;


    @TableField("work_order_subject")
    private String workOrderSubject;

    @TableField("work_order_content")
    private String workOrderContent;

    @TableField("milestone_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date milestoneDate;


    @TableField("req_complete_date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reqCompleteDate;

    @TableField("create_by_id")
    private String createById;

    @TableField("create_by")
    private String createBy;

    @TableField("create_date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createDate;

    /**
     *
     * 已派单：定单已经派出，主办人还没有接单。
     * 处理中：主办人已接单
     * 阶段回单：主办人已阶段回单
     * 已回单：主办人已回单
     * 已归档：派单人已经回单评审后，流程结束。
     *
     */
    @TableField("status")
    private String status;


    @TableField("flow_Info_Id")
    private String flowInfoId;

    @TableField("flow_stage")
    private String flowStage;

    @TableField("flow_stage_name")
    private String flowStageName;


    @TableField("editable")
    private String editable;


    @TableField(exist = false)
    private String watched;

    @TableField(exist = false)
    private String checkOut;

    @TableField(exist = false)
    private String checkIn;

    @TableField("attach_file")
    private String attachFile;

    @TableField(exist = false)
    private String canDeal;

    //定单类型 用来区分事务单 1、营销单 2、派车单 3
    private String orderType;

    //图片类型 用来区分事务单: icon_thing 营销单: icon_market 派车单: icon_car
    private String orderIcon;

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getEmergencyLevel() {
        return emergencyLevel;
    }

    public void setEmergencyLevel(String emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }

    public String getCanDeal() {
        return canDeal;
    }

    public void setCanDeal(String canDeal) {
        this.canDeal = canDeal;
    }

    public String getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(String attachFile) {
        this.attachFile = attachFile;
    }

    public String getWatched() {
        return watched;
    }

    public void setWatched(String watched) {
        this.watched = watched;
    }

    public String getOrganiserName() {
        return organiserName;
    }

    public void setOrganiserName(String organiserName) {
        this.organiserName = organiserName;
    }

    public String getCoOrganiserName() {
        return coOrganiserName;
    }

    public void setCoOrganiserName(String coOrganiserName) {
        this.coOrganiserName = coOrganiserName;
    }

    public String getFlowStageName() {
        return flowStageName;
    }

    public void setFlowStageName(String flowStageName) {
        this.flowStageName = flowStageName;
    }

    public String getFlowInfoId() {
        return flowInfoId;
    }

    public void setFlowInfoId(String flowInfoId) {
        this.flowInfoId = flowInfoId;
    }

    public String getFlowStage() {
        return flowStage;
    }

    public void setFlowStage(String flowStage) {
        this.flowStage = flowStage;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }


    public String getWorkOrderCode() {
        return workOrderCode;
    }

    public void setWorkOrderCode(String workOrderCode) {
        this.workOrderCode = workOrderCode;
    }

    /**
     * 工单是否已读
     */
    @TableField(exist = false)
    private String readStatus;

    @Override
    protected Serializable pkVal() {
        return this.workOrderId;
    }


    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getOrganiser() {
        return organiser;
    }

    public void setOrganiser(String organiser) {
        this.organiser = organiser;
    }

    public String getCoOrganiser() {
        return coOrganiser;
    }

    public void setCoOrganiser(String coOrganiser) {
        this.coOrganiser = coOrganiser;
    }

    public String getWorkOrderType() {
        return workOrderType;
    }

    public void setWorkOrderType(String workOrderType) {
        this.workOrderType = workOrderType;
    }

    public String getWorkOrderSubject() {
        return workOrderSubject;
    }

    public void setWorkOrderSubject(String workOrderSubject) {
        this.workOrderSubject = workOrderSubject;
    }

    public String getWorkOrderContent() {
        return workOrderContent;
    }

    public void setWorkOrderContent(String workOrderContent) {
        this.workOrderContent = workOrderContent;
    }

    public Date getMilestoneDate() {
        return milestoneDate;
    }

    public void setMilestoneDate(Date milestoneDate) {
        this.milestoneDate = milestoneDate;
    }

    public Date getReqCompleteDate() {
        return reqCompleteDate;
    }

    public void setReqCompleteDate(Date reqCompleteDate) {
        this.reqCompleteDate = reqCompleteDate;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderIcon() {
        return orderIcon;
    }

    public void setOrderIcon(String orderIcon) {
        this.orderIcon = orderIcon;
    }

}
