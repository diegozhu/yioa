package com.yioa.sys.service;

import com.yioa.sys.domain.SysUser;

/**
 * Created by tao on 2017-05-22.
 */
public interface SmsBusiService {

    public boolean isMobileExsit(String mobileNum);


    public SysUser getUserIdByNum(String num) ;


}
