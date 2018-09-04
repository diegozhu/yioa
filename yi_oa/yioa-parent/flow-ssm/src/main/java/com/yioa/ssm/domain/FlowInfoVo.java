package com.yioa.ssm.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tao on 2017-05-28.
 */
@TableName("t_flow_info")
public class FlowInfoVo extends Model<FlowInfoVo> {

    @TableId("flow_info_Id")
    private String flowInfoId;

    @TableField("work_order_id")
    private String workOrderId;


    @TableField("prev_flow_info_id")
    private String prevFlowInfoId;

    @TableField("flow_stage")
    private String flowStage;

    @TableField("flow_stage_name")
    private String flowStageName;

    /**
     * 派单信息，处理人name
     */
    @TableField("handle_by")
    private String handleBy;

    /**
     * 派单信息，处理人id
     */
    @TableField("handle_by_id")
    private String handleById;


    /**
     * 派单信息，处理人类型，扩展用，暂时都是人
     */
    @TableField("handle_by_type")
    private String handleByType;


    /**
     * 收单人
     */
    @TableField("receive_user_id")
    private String receiveUserId;

    /**
     * 收单时间
     */
    @TableField("recieve_date")
    private Date recieveDate;


    /**
     * 本环节实际处理人id
     */
    @TableField("complete_user_id")
    private String completeUserId;

    /**
     * 本环节实际处理人
     */
    @TableField("complete_user_name")
    private String completeUserName;

    /**
     * 本环节结束时间
     */
    @TableField("complete_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date completeDate;


    @TableField("create_date")
    private Date createDate;


    @TableField("notes")
    private String notes;

    @TableField("attach_file")
    private String attachFile;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @TableField("address")
    private String address;

    @TableField("longitude")
    private String longitude;

    @TableField("latitude")
    private String latitude;

    public String getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(String attachFile) {
        this.attachFile = attachFile;
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

    public String getHandleByType() {
        return handleByType;
    }

    public void setHandleByType(String handleByType) {
        this.handleByType = handleByType;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getCompleteUserId() {
        return completeUserId;
    }

    public void setCompleteUserId(String completeUserId) {
        this.completeUserId = completeUserId;
    }

    public String getCompleteUserName() {
        return completeUserName;
    }

    public void setCompleteUserName(String completeUserName) {
        this.completeUserName = completeUserName;
    }


    public String getPrevFlowInfoId() {
        return prevFlowInfoId;
    }

    public void setPrevFlowInfoId(String prevFlowInfoId) {
        this.prevFlowInfoId = prevFlowInfoId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    protected Serializable pkVal() {
        return this.flowInfoId;
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

    public String getFlowStageName() {
        return flowStageName;
    }

    public void setFlowStageName(String flowStageName) {
        this.flowStageName = flowStageName;
    }


    public Date getRecieveDate() {
        return recieveDate;
    }

    public void setRecieveDate(Date recieveDate) {
        this.recieveDate = recieveDate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

}
