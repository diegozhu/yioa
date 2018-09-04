package com.xseed.sms.controller;

import com.xseed.sms.service.SmsService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by tao on 2017-05-03.
 */
@RestController
@RequestMapping("/api/sms")
@Deprecated
public class SmsController {

    @Autowired
    private SmsService smsService;


    @ApiOperation(value="校验短信验证码", notes="对提交的手机号和短信验证码进行验证，返回true和false, 本api应该在内网调用，而不是公网")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "短信验证码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "num", value = "手机号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/verify/{code}/{num}", method = RequestMethod.GET)
    @Deprecated
    public String validSms(@PathVariable String code, @PathVariable String num, HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        return smsService.valid(session.getId(), code, num);
    }



    @ApiOperation(value="发送短信验证码", notes="向提交的手机号发送验证码，返回网关发送的消息，本api应该在内网调用，而不是公网")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "验证码接收者(显示名)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "num", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "seq", value = "会话标识", required = true, dataType = "String")
    })
    @RequestMapping(value = "/send/{name}/{num}/{seq}", method = RequestMethod.GET)
    @Deprecated
    public String sendSms(@PathVariable String name, @PathVariable String num, @PathVariable String seq) {
        return smsService.sendWithName(seq, name, num);
    }

}
