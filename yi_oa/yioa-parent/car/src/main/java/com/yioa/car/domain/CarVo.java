package com.yioa.car.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * Created by tao on 2017-06-04.
 */
@TableName("t_car")
public class CarVo  extends Model<CarVo>{

    @TableId("car_id")
    private String carId;

    @TableField("car_no")
    private String carNo;

    @TableField("car_type")
    private String carType;

    @TableField("delete_tag")
    private String deleteTag;

    @TableField("capacity")
    private int capacity;

    @TableField(exist = false)
    private String carSendOrderId;

    @TableField(exist = false)
    private String occupied;

    @TableField(exist = false)
    private String passengerName;

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getCarSendOrderId() {
        return carSendOrderId;
    }

    public void setCarSendOrderId(String carSendOrderId) {
        this.carSendOrderId = carSendOrderId;
    }

    public String getOccupied() {
        return occupied;
    }

    public void setOccupied(String occupied) {
        this.occupied = occupied;
    }

    @Override
    protected Serializable pkVal() {
        return this.carId;
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

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getDeleteTag() {
        return deleteTag;
    }

    public void setDeleteTag(String deleteTag) {
        this.deleteTag = deleteTag;
    }
}
