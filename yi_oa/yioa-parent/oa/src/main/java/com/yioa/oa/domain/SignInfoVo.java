package com.yioa.oa.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liangpengcheng on 2018/5/15.
 */
@TableName("t_sign_info")
public class SignInfoVo extends Model<SignInfoVo> {

    @TableId("work_order_id")
    private String work_order_id;

    @TableField("address")
    private String address;

    @TableField("longitede")
    private String longitude;

    @TableField("latitude")
    private String latitude;

    @TableField("create_date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createDate;

    @Override
    protected Serializable pkVal() {
        return this.work_order_id;
    }


    public String getWork_order_id() {
        return work_order_id;
    }

    public void setWork_order_id(String work_order_id) {
        this.work_order_id = work_order_id;
    }

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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
