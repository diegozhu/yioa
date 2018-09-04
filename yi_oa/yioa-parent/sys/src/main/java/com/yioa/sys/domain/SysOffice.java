package com.yioa.sys.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;


/**
 * Created by liangpengcheng on 2018/3/10.
 */
@TableName("SysOffice")
public class SysOffice extends Model<SysUser>{

    @TableId
    private String id;
    @TableField("name")
    private String name;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}
