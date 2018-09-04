package com.yioa.core.controller;


import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.ImmutableMap;
import com.xseed.xoauth.util.OauthConst;
import com.yioa.common.exception.YiException;
import com.yioa.common.util.CommonCnst;
import com.yioa.common.util.CommonUtil;
import com.yioa.core.mapper.WatchOrderMapper;
import com.yioa.sys.domain.SysRole;
import com.yioa.sys.domain.SysUser;
import com.yioa.sys.domain.UserOpenidVo;
/**
 * Created by bysocket on 07/02/2017.
 */
@RestController
public class OaDemoController {


    private final static Logger logger = LoggerFactory.getLogger(OaDemoController.class);


    @Value("${xseed.hi}")
    private String histr;

    @RequestMapping(value = "/api/test/{name}", method = RequestMethod.GET)
    public String test(@PathVariable String name) {
        return "hi, " + name;
    }
    @Autowired
    private WatchOrderMapper watchOrderMapper;

    @RequestMapping(value = "/login4test/{loginName}", method = RequestMethod.GET)
    public String login4test(@PathVariable String loginName, HttpServletRequest request) throws IOException {

    	logger.error("login4test " + loginName);
        SysUser user = new SysUser();
        user = user.selectOne(new EntityWrapper<SysUser>().eq("login_name", loginName));

        UserOpenidVo vo = new UserOpenidVo();
        vo.setId(user.getId());
        vo = vo.selectById();

        WebUtils.setSessionAttribute(request, OauthConst.OPENID, vo.getOpenid());
        WebUtils.setSessionAttribute(request, CommonCnst.USER_ID, user.getId());
        WebUtils.setSessionAttribute(request, CommonCnst.USER_LOGIN_NAME, user.getLoginName());
        WebUtils.setSessionAttribute(request, CommonCnst.USER_INFO, user);
        logger.info("############# {} login sucess,goto welcome", loginName);

        return "set login success";
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void login(HttpServletRequest request,HttpServletResponse response) throws IOException {
        Object val = WebUtils.getSessionAttribute(request, CommonCnst.USER_ID);
        if(val == null){
            response.sendRedirect("http://localhost/wap/yioa/login.html");
        }else{
            response.sendRedirect("http://localhost/wap/yioa/me.html");
        }
//        logger.info("############# : login sucess,goto welcome");
//        response.sendRedirect("http://localhost/wap/yioa/login.html");
//        response.sendRedirect("http://111.231.69.67/b/yioa/welcome.html");
    }

    /**
     * 登录
     * @param username
     * @param password
     * @param request
     * @return
     * @throws YiException
     */
    @RequestMapping(value = "/dologin/{username}/{password}",method = RequestMethod.GET)
    public SysUser dologin(@PathVariable String username, @PathVariable String password, HttpServletRequest request)throws YiException {
        System.out.println("用户名是" + username + "密码是" + password);
        SysUser user = new SysUser();
        user = user.selectOne(new EntityWrapper<SysUser>().eq("login_name", username));
        if(user != null){
            boolean login = CommonUtil.validatePassword(password,user.getPassword());
            if(login){
                UserOpenidVo vo = new UserOpenidVo();
                vo.setId(user.getId());
                vo = vo.selectById();
                Map<String, Object> tMap = ImmutableMap.of("userId", user.getId());
                List<SysRole> list = this.watchOrderMapper.queryRoleByUserId(tMap);
                user.setRoleList(list);
                WebUtils.setSessionAttribute(request, OauthConst.OPENID, vo.getOpenid());
                WebUtils.setSessionAttribute(request, CommonCnst.USER_ID, user.getId());
                WebUtils.setSessionAttribute(request, CommonCnst.USER_LOGIN_NAME, user.getLoginName());
                WebUtils.setSessionAttribute(request, CommonCnst.USER_INFO, user);
                user.setLogin_flag("0");
                return user;
            }else{
                user.setLogin_flag("1");
                return user;
            }
        }else{
            user.setLogin_flag("1");
            return user;
        }
    }

    @RequestMapping(value = "/loginout", method = RequestMethod.GET)
    public SysUser loginout(HttpServletRequest request)throws YiException{
        SysUser user = new SysUser();
        Enumeration<String> em = request.getSession().getAttributeNames();
        while(em.hasMoreElements()){
            request.getSession().removeAttribute(em.nextElement());
        }
        request.getSession().removeAttribute(OauthConst.OPENID);
        request.getSession().removeAttribute(CommonCnst.USER_ID);
        request.getSession().removeAttribute(CommonCnst.USER_LOGIN_NAME);
        request.getSession().removeAttribute(CommonCnst.USER_INFO);

        request.getSession().invalidate();
        user.setLogin_flag("1");
        return user;
    }


    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    public String hi(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce, @RequestParam("echostr") String echostr) {


        logger.info("############# : signature:" + signature);
        logger.info("############# : timestamp:" + timestamp);
        logger.info("############# : nonce:" + nonce);
        logger.info("############# : echostr:" + echostr);

        return echostr;
    }

}
