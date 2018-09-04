package com.yioa.market.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by liangpengcheng on 2018/5/5.
 */
@TableName("t_market_order")
public class MarketOrderVo extends Model<MarketOrderVo>{

    @TableId("market_send_order_id")
    private String market_send_order_id;

    @TableField("market_send_order_code")
    private String market_send_order_code;

    @TableField("work_order_subject")
    private String work_order_subject;
    

    @TableField("passenger")
    private String passenger;

    @TableField("passenger_name")
    private String passenger_name;

    @TableField("co_organiser")
    private String co_organiser;

    @TableField("co_organiser_name")
    private String co_organiser_name;
    
    @TableField("lat")
    private String lat;
    
    @TableField("lng")
    private String lng;

    @TableField("destination")
    private String destination;

    @TableField("market_area_ame")
    private String market_area_ame;

    @TableField("market_targer_name")
    private String market_targer_name;

    @TableField("plan_leave_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date plan_leave_date;

    @TableField("plan_back_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date plan_back_date;

    @TableField("wo_order_content")
    private String wo_order_content;

    @TableField("attach_file")
    private String attachFile;

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
    
    public String getWatched() {
		return watched;
	}

	public void setWatched(String watched) {
		this.watched = watched;
	}

	public String getMarket_send_order_id() {
        return market_send_order_id;
    }

    public void setMarket_send_order_id(String market_send_order_id) {
        this.market_send_order_id = market_send_order_id;
    }

    public String getMarket_send_order_code() {
        return market_send_order_code;
    }

    public void setMarket_send_order_code(String market_send_order_code) {
        this.market_send_order_code = market_send_order_code;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public String getPassenger_name() {
        return passenger_name;
    }

    public void setPassenger_name(String passenger_name) {
        this.passenger_name = passenger_name;
    }

    public String getCo_organiser() {
        return co_organiser;
    }

    public void setCo_organiser(String co_organiser) {
        this.co_organiser = co_organiser;
    }

    public String getCo_organiser_name() {
        return co_organiser_name;
    }

    public void setCo_organiser_name(String co_organiser_name) {
        this.co_organiser_name = co_organiser_name;
    }

    public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMarket_area_ame() {
        return market_area_ame;
    }

    public void setMarket_area_ame(String market_area_ame) {
        this.market_area_ame = market_area_ame;
    }

    public String getMarket_targer_name() {
        return market_targer_name;
    }

    public void setMarket_targer_name(String market_targer_name) {
        this.market_targer_name = market_targer_name;
    }

    public Date getPlan_leave_date() {
        return plan_leave_date;
    }

    public void setPlan_leave_date(Date plan_leave_date) {
        this.plan_leave_date = plan_leave_date;
    }

    public Date getPlan_back_date() {
        return plan_back_date;
    }

    public void setPlan_back_date(Date plan_back_date) {
        this.plan_back_date = plan_back_date;
    }

    public String getWo_order_content() {
        return wo_order_content;
    }

    public void setWo_order_content(String wo_order_content) {
        this.wo_order_content = wo_order_content;
    }

    public String getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(String attachFile) {
        this.attachFile = attachFile;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    @Override
    protected Serializable pkVal() {
        return this.market_send_order_id;
    }

    public String getWork_order_subject() {
        return work_order_subject;
    }

    public void setWork_order_subject(String work_order_subject) {
        this.work_order_subject = work_order_subject;
    }

}
