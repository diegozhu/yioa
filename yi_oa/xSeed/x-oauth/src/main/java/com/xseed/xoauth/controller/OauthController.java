package com.xseed.xoauth.controller;

import com.xseed.xoauth.domain.AccessTokenResponse;
import com.xseed.xoauth.service.OauthService;
import com.xseed.xoauth.util.OauthConst;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by tao on 2017-05-18.
 */
@RestController
@RequestMapping("/api/oauth")
//不太明白，为何这个类打了个Deprecated，尴尬了，想不起来
@Deprecated
public class OauthController {

    @Value("${xseed.oauth.yixin.openid_path}")
    private  String tokenPath;


    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private OauthService oauthService;

    /**
     *
     * @param code 易信返回的code
     * @param redurl 获取openid成功之后跳转的url
     * @param request
     * @param response
     * @throws IOException
     */
    @ApiOperation(value="获取 openid", notes="获取 openid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "易信返回的code", required = true, dataType = "String")
    })
    @RequestMapping(value = "/openid", method = RequestMethod.GET)
    public void getOpenid(@RequestParam String code,@RequestParam  String redurl ,HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(StringUtils.isEmpty(code)){
            response.sendRedirect("/error");
        }


        //FIXME 这里成功获取了openid 和 accessToken，需要记录到系统中
        // openid 应该做持久化，accessToken 有24小时的有效期，应该缓存20小时，搞个redis？？

        AccessTokenResponse accessTokenResponse = oauthService.accessYiXin(code);

        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute(OauthConst.OPENID,accessTokenResponse.getOpenid());

        response.sendRedirect(URLDecoder.decode(redurl,OauthConst.UTF8));
    }

}
