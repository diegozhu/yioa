package com.yioa.car.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by tao on 2017-06-04.
 */
@TableName("t_car_send_order")
public class CarSendOrderVo extends Model<CarSendOrderVo> {

    @TableId("car_send_order_id")
    private String carSendOrderId;

    @TableField("car_send_order_code")
    private String carSendOrderCode;

    @TableField("driver_id")
    private String driverId;

    @TableField(exist = false)
    private String driverPhone;
    
    @TableField("driver_name")
    private String driverName;

    @TableField("car_id")
    private String carId;

    @TableField("car_no")
    private String carNo;

    @TableField("passenger_id")
    private String passengerId;

    @TableField("passenger_name")
    private String passengerName;


    @TableField("feedback_id")
    private String feedbackId;


    @TableField("feedback_name")
    private String feedbackName;

    @TableField("destination")
    private String destination;

    @TableField("plan_leave_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date planLeaveDate;

    @TableField("plan_back_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date planBackDate;


    @TableField("car_send_order_subject")
    private String carSendOrderSubject;


    @TableField("car_send_order_content")
    private String carSendOrderContent;

    @TableField("create_by_id")
    private String createById;

    @TableField("create_by")
    private String createBy;

    @TableField("create_date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createDate;

    @TableField("status")
    private String status;

    @TableField("flow_Info_Id")
    private String flowInfoId;

    @TableField("flow_stage")
    private String flowStage;

    @TableField("flow_stage_name")
    private String flowStageName;

    @TableField(exist = false)
    private String watched;

    @TableField("attach_file")
    private String attachFile;

    @TableField(exist = false)
    private String checkOut;

    @TableField(exist = false)
    private String checkIn;

    @TableField("editable")
    private String  editable;

    public String getCheckOut() {
        return checkOut;
    }

    public String getDriverPhone() {
		return driverPhone;
	}

	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}

	public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }
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

    public String getFlowStageName() {
        return flowStageName;
    }

    public void setFlowStageName(String flowStageName) {
        this.flowStageName = flowStageName;
    }

    public String getCarSendOrderSubject() {
        return carSendOrderSubject;
    }

    public void setCarSendOrderSubject(String carSendOrderSubject) {
        this.carSendOrderSubject = carSendOrderSubject;
    }

    @Override
    protected Serializable pkVal() {
        return this.carSendOrderId;
    }

    public String getCarSendOrderId() {
        return carSendOrderId;
    }

    public void setCarSendOrderId(String carSendOrderId) {
        this.carSendOrderId = carSendOrderId;
    }

    public String getCarSendOrderCode() {
        return carSendOrderCode;
    }

    public void setCarSendOrderCode(String carSendOrderCode) {
        this.carSendOrderCode = carSendOrderCode;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getFeedbackName() {
        return feedbackName;
    }

    public void setFeedbackName(String feedbackName) {
        this.feedbackName = feedbackName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getPlanLeaveDate() {
        return planLeaveDate;
    }

    public void setPlanLeaveDate(Date planLeaveDate) {
        this.planLeaveDate = planLeaveDate;
    }

    public Date getPlanBackDate() {
        return planBackDate;
    }

    public void setPlanBackDate(Date planBackDate) {
        this.planBackDate = planBackDate;
    }

    public String getCarSendOrderContent() {
        return carSendOrderContent;
    }

    public void setCarSendOrderContent(String carSendOrderContent) {
        this.carSendOrderContent = carSendOrderContent;
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
}
