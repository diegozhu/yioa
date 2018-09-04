package com.yioa.sys.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by tao on 2017-05-22.
 */
@TableName("sys_user")
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    @TableField("company_id")
    private String companyId;

    @TableField("office_id")
    private String officeId;

    @TableField("login_name")
    private String loginName;// 登录名

    @TableField("password")
    private String password;// 密码

    @TableField("no")
    private String no;        // 工号

    @TableField("name")
    private String name;    // 姓名

    @TableField("email")
    private String email;    // 邮箱

    @TableField("phone")
    private String phone;    // 电话

    @TableField("mobile")
    private String mobile;    // 手机

    @TableField("user_type")
    private String userType;// 用户类型

    @TableField("login_ip")
    private String loginIp;    // 最后登陆IP

    @TableField("login_date")
    private Date loginDate;    // 最后登陆日期

    @TableField("login_flag")
    private String loginFlag;    // 是否允许登陆

    @TableField("create_by")
    private String createBy;    // 创建人id

    @TableField("create_date")
    private Date createDate;    // 创建日期

    @TableField("update_by")
    private String updateBy;    //最后修改人

    @TableField("update_date")
    private Date updateDate;    // 最后修改日期

    @TableField("remarks")
    private String remarks;    // 备注

    @TableField("del_flag")
    private String delFlag;    // 删除标识 0 正常；1 删除

    @TableField("driver_flag")
    private String driverFlag;    // 删除标识 0 正常；1 删除

    @TableField("login_flag")
    private String login_flag;    //登录成功 0 登录失败 1


    @TableField(exist = false)
    private List<SysRole> roleList;

    @TableField(exist = false)
    private String orgName;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<SysRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SysRole> roleList) {
        this.roleList = roleList;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public String getDriverFlag() {
        return driverFlag;
    }

    public void setDriverFlag(String driverFlag) {
        this.driverFlag = driverFlag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
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

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getLogin_flag() {
        return login_flag;
    }

    public void setLogin_flag(String login_flag) {
        this.login_flag = login_flag;
    }
    //    @TableField("photo")
//    private String photo;	// 头像
//
//    @TableField("no")
//    private String oldLoginName;// 原登录名
//
//    @TableField("no")
//    private String newPassword;	// 新密码
//
//    @TableField("no")
//    private String oldLoginIp;	// 上次登陆IP
//
//    @TableField("no")
//    private Date oldLoginDate;	// 上次登陆日期

}
