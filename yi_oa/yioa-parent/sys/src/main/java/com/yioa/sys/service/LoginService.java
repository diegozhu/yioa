package com.yioa.sys.service;

import com.yioa.sys.domain.UserOpenidVo;
import org.springframework.stereotype.Service;

/**
 * Created by tao on 2017-05-22.
 */
@Service
public class LoginService {


    public void saveOpenid(String openid,String userId,String loginName){
        UserOpenidVo userOpenidVo = new UserOpenidVo();

        userOpenidVo.setId(userId);
        userOpenidVo.setLoginName(loginName);
        userOpenidVo.setOpenid(openid);
        userOpenidVo.insertOrUpdate();

    }
}
