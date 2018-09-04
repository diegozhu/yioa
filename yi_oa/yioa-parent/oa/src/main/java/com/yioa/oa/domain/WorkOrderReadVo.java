package com.yioa.oa.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 *
 * 这个表废弃了,代码先留几个版本
 * Created by tao on 2017-05-24.
 */
@Deprecated
@TableName("work_order_read")
public class WorkOrderReadVo extends Model<WorkOrderReadVo> {

    @TableId
    private String workOrderReadId;


    @TableField("flow_info_id")
    private String flowInfoId;

    @TableField("read_user_id")
    private String readUserId;


    @TableField(exist = false)
    private String readStaus = "1";


    public WorkOrderReadVo( ) {
        super();
        this.readStaus = "1";
    }

    public WorkOrderReadVo(String flowInfoId, String readUserId) {
        super();
        this.flowInfoId = flowInfoId;
        this.readUserId = readUserId;
        this.readStaus = "1";
    }

    @Override
    protected Serializable pkVal() {
        return this.workOrderReadId;
    }


    public String getWorkOrderReadId() {
        return workOrderReadId;
    }

    public void setWorkOrderReadId(String workOrderReadId) {
        this.workOrderReadId = workOrderReadId;
    }

    public String getFlowInfoId() {
        return flowInfoId;
    }

    public void setFlowInfoId(String flowInfoId) {
        this.flowInfoId = flowInfoId;
    }

    public String getReadUserId() {
        return readUserId;
    }

    public void setReadUserId(String readUserId) {
        this.readUserId = readUserId;
    }

    public String getReadStaus() {
        return readStaus;
    }

    public void setReadStaus(String readStaus) {
        this.readStaus = readStaus;
    }


}
