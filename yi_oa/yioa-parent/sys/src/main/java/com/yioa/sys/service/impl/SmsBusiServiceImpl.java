package com.yioa.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.yioa.sys.domain.SysUser;
import com.yioa.sys.service.SmsBusiService;
import org.springframework.stereotype.Service;

/**
 * Created by tao on 2017-05-22.
 */
@Service
public class SmsBusiServiceImpl implements SmsBusiService{


    @Override
    public boolean isMobileExsit(String mobileNum) {

        SysUser user = new SysUser();
//        user.setMobile(mobileNum);
        user =  user.selectOne(new EntityWrapper<SysUser>().eq("mobile",mobileNum));
        return user != null;
    }


    public SysUser getUserIdByNum(String num) {

        SysUser user = new SysUser();
//        user.setMobile(mobileNum);
        user =  user.selectOne(new EntityWrapper<SysUser>().eq("mobile",num));
        return user;
    }


}
