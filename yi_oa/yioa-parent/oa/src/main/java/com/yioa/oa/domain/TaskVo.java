package com.yioa.oa.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

@TableName("t_my_task")
public class TaskVo extends Model<WatchOrderVo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
    @TableId("task_id")
	private String taskId;
	
	@TableField("task_level")
	private String taskLevel;
	
	@TableField("is_deleted")
	private String isDeleted;
	
	@TableField("start_time")
    private String startTime;
	
	@TableField("end_time")
    private String endTime;
	
	@TableField("alert")
	private String alert;
	
	@TableField("alert_time")
	private String alertTime;
	
	@TableField("alert_result")
	private String alertResult;
	
	@TableField("project_name")
	private String projectName;
	
	@TableField("create_by")
	private String createBy;
	
	@TableField("create_by_id")
	private String createById;
	
	@TableField("status")
	private String status;
	
	@TableField("editable")
	private String editable;
	
	@TableField("ref_order")
	private String refOrder;
	
	@TableField("ref_order_id")
	private String refOrderId;
	
	@TableField("ref_order_type")
	private String refOrderType;
	
	
	@TableField("task_content")
	private String taskContent;
	
	
	@TableField("task_name")
	private String taskName;
	
	
	
	public String getAlertTime() {
		return alertTime;
	}



	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
	}



	public String getTaskContent() {
		return taskContent;
	}



	public String getAlertResult() {
		return alertResult;
	}



	public void setAlertResult(String alertResult) {
		this.alertResult = alertResult;
	}



	public String getProjectName() {
		return projectName;
	}



	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}



	public String getRefOrderType() {
		return refOrderType;
	}



	public void setRefOrderType(String refOrderType) {
		this.refOrderType = refOrderType;
	}



	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}



	public String getCreateById() {
		return createById;
	}



	public String getRefOrderId() {
		return refOrderId;
	}



	public void setRefOrderId(String refOrderId) {
		this.refOrderId = refOrderId;
	}



	public void setCreateById(String createById) {
		this.createById = createById;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public String getIsDeleted() {
		return isDeleted;
	}



	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}



	public String getTaskName() {
		return taskName;
	}



	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}



	@TableField("create_date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
	
	
	
	public String getTaskId() {
		return taskId;
	}



	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}



	public String getTaskLevel() {
		return taskLevel;
	}



	public void setTaskLevel(String taskLevel) {
		this.taskLevel = taskLevel;
	}



	public String getStartTime() {
		return startTime;
	}



	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}



	public String getEndTime() {
		return endTime;
	}



	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}



	public String getAlert() {
		return alert;
	}



	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getCreateBy() {
		return createBy;
	}



	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getEditable() {
		return editable;
	}



	public void setEditable(String editable) {
		this.editable = editable;
	}



	public String getRefOrder() {
		return refOrder;
	}



	public void setRefOrder(String refOrder) {
		this.refOrder = refOrder;
	}



	public Date getCreateDate() {
		return createDate;
	}



	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	@Override
	protected Serializable pkVal() {
		return this.taskId;
	}

}
