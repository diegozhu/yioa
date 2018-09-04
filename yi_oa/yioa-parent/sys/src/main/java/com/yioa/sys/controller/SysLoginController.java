package com.yioa.sys.controller;

import com.yioa.sys.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by tao on 2017-05-22.
 */
@RestController
@RequestMapping("/oa/sys")
//       /oa/sys/access_callback
@Deprecated
public class SysLoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 记录回调，
     * @param openid
     * @param access_token
     * @param refresh_token
     * @param expires_in
     * @param scope
     * @return
     */
    @RequestMapping(value = "/access_callback/{openid}/{access_token}/{refresh_token}/{expires_in}/{scope}",method = RequestMethod.GET)
    public String accessCallback(@PathVariable String openid, @PathVariable String access_token, @PathVariable String refresh_token, @PathVariable String expires_in, @PathVariable String scope,HttpServletRequest request) {

        HttpSession session = request.getSession(false);

//        loginService.saveOpenid(openid,);

        return "";
    }
}
