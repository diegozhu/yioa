package com.yioa.sys.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * Created by tao on 2017-05-22.
 */
@TableName("t_user_openid")
public class UserOpenidVo extends Model<UserOpenidVo> {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    @TableField("login_name")
    private String loginName;

    @TableField("openid")
    private String openid;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
