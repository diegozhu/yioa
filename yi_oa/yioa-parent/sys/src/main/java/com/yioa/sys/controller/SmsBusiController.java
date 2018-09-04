package com.yioa.sys.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;

import com.xseed.sms.service.SmsService;
import com.xseed.xoauth.util.OauthConst;
import com.yioa.common.cfg.PathService;
import com.yioa.common.exception.YiException;
import com.yioa.common.util.CommonCnst;
import com.yioa.sys.domain.SysUser;
import com.yioa.sys.service.LoginService;
import com.yioa.sys.service.SmsBusiService;
import com.yioa.sys.service.SysUserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Created by tao on 2017-05-22.
 */
@RequestMapping("/oa/sms")
@RestController
public class SmsBusiController {

    private final static Logger logger = LoggerFactory.getLogger(SmsBusiController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PathService pathService;

    @Autowired
    private SmsService smsService;


    @Autowired
    private LoginService loginService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SmsBusiService smsBusiService;

//    private String makeRequestURL(HttpServletRequest request, String pre) {
//        String path = request.getContextPath();
//        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/" + pre;
//        return basePath + request.getRequestURI() + "?" + request.getQueryString();
//    }
//
//
//    @RequestMapping(value = "/test/{num}", method = RequestMethod.GET)
//    public String test(@PathVariable String num, HttpServletRequest request) throws YiException {
//
//        logger.info("### {}", request.getAuthType());
//        logger.info("### {}", request.getContextPath());
//        logger.info("### {}", request.getPathInfo());
//        logger.info("### {}", request.getPathTranslated());
//        logger.info("### {}", request.getQueryString());
//        logger.info("### {}", request.getRemoteUser());
//        logger.info("### {}", request.getRequestURI());
//        logger.info("### {}", request.getRequestURL());
//        logger.info("### {}", request.getServletPath());
//        logger.info("### {}", request.getScheme());
//        logger.info("### {}", request.getServletContext());
//
//
//        String url = makeRequestURL(request, "a");
//        return url;
//    }


    @ApiOperation(value = "发送短信验证码", notes = "向提交的手机号发送验证码，返回网关发送的消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "验证码接收者(显示名)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "num", value = "手机号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/send/{name}/{num}", method = RequestMethod.GET)
    /**
     * 发送短信之前，需要校验号码是否在数据库里面
     */
    public String sendSms(@PathVariable String name, @PathVariable String num, HttpServletRequest request) throws YiException {


        HttpSession session = request.getSession(false);

        boolean bool = smsBusiService.isMobileExsit(num);

        //号码不存，返回错误
//        if (!bool) {
//            throw new YiException(SysErrorCnst.MOBILE_NOT_EXSITS, SysErrorCnst.MSG_MOBILE_NOT_EXSITS);
//        }

        String reStr = smsService.sendWithName(session.getId(), name, num);

        //发送失败，返回错误
        if (!reStr.equalsIgnoreCase("1")) {
            throw new YiException("", "");
        }

        return "1";
    }


    @Deprecated
    @ApiOperation(value = "校验短信验证码", notes = "对提交的手机号和短信验证码进行验证，返回true和false")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "短信验证码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "num", value = "手机号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/verify/{code}/{num}", method = RequestMethod.GET)
    public String validSms(@PathVariable String code, @PathVariable String num, HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        String reStr = smsService.valid(session.getId(), code, num);
        if (!reStr.equalsIgnoreCase("1")) {
            return reStr;
        }

        //验证通过，获取用户id，放入session
        SysUser user = smsBusiService.getUserIdByNum(num);
        session.setAttribute(CommonCnst.USER_ID, user.getId());
        session.setAttribute(CommonCnst.USER_LOGIN_NAME, user.getLoginName());
        session.setAttribute(CommonCnst.USER_INFO, user);


        //持久化信息
        String openid = String.valueOf(WebUtils.getSessionAttribute(request, OauthConst.OPENID));

        loginService.saveOpenid(openid, user.getId(), user.getLoginName());
        return "1";
    }

    @Deprecated
    @ApiOperation(value = "校验短信验证码", notes = "对提交的手机号和短信验证码进行验证，返回true和false")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "btnCode", value = "短信验证码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "tel", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "inputTel", value = "手机号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/changePhone/{btnCode}/{tel}/{inputTel}", method = RequestMethod.GET)
    public String changePhone(@PathVariable String btnCode, @PathVariable String tel, @PathVariable String inputTel, HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        String reStr = smsService.valid(session.getId(), btnCode, inputTel);
        if (!reStr.equalsIgnoreCase("1")) {
            return reStr;
        }

        //验证通过，获取用户id，放入session
        SysUser user = smsBusiService.getUserIdByNum(tel);

        sysUserService.updateMobile( user.getId(), inputTel);
        return "1";
    }


}
