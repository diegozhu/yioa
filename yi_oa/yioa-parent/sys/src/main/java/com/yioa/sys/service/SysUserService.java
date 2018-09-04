package com.yioa.sys.service;

import com.yioa.sys.domain.SysUser;
import com.yioa.sys.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by tao on 2017-06-04.
 */
@Service
public class SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    public List<SysUser> getUserById(List<String> idList) {

        Assert.notNull(idList, "id is not null");
        List<SysUser> userList = sysUserMapper.selectBatchIds(idList);
        Assert.notNull(userList, "userList is not null");
        return userList;
    }

    public void updateMobile(String userId,String mobile){
        SysUser sysUser = new SysUser();
        sysUser.setId(userId);
        sysUser.setMobile(mobile);
        sysUser.insertOrUpdate();

    }

}
