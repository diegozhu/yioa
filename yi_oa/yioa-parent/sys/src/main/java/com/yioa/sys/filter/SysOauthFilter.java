package com.yioa.sys.filter;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.util.WebUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xseed.xoauth.filter.OauthFilter;
import com.xseed.xoauth.util.OauthConst;
import com.yioa.common.util.CommonCnst;
import com.yioa.common.util.CommonUtil;
import com.yioa.sys.domain.SysUser;
import com.yioa.sys.domain.UserOpenidVo;


/**
 * Created by tao on 2017-06-15.
 */
public class SysOauthFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(OauthFilter.class);

    private String bindUserUrl;


    @Autowired
    private String[] whiteListUrlArr;


    public String[] getWhiteListUrlArr() {
        return whiteListUrlArr;
    }

    public void setWhiteListUrlArr(String[] whiteListUrlArr) {
        this.whiteListUrlArr = whiteListUrlArr;
    }


    public String getBindUserUrl() {
        return bindUserUrl;
    }

    public void setBindUserUrl(String bindUserUrl) {
        this.bindUserUrl = bindUserUrl;
    }

    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
        logger.info("########### SysOauthFilter inited");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        boolean bool = initUser(req, res);
        if (bool) {
            chain.doFilter(req, res);
        }

    }

    /**
     * 从session中取得openid之后:
     * 1 如果已经注册过，则在session中加入userId和userName放行
     * 2 重定向到注册界面
     *
     * @param req
     * @param res
     * @throws IOException
     */
    private boolean initUser(ServletRequest req, ServletResponse res) throws IOException {

        //首先尝试从session中获取登陆成功的标识，如果不存在，认为没有登陆

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String requestUrl = request.getRequestURI();

        logger.info("#######requestUrl: " + requestUrl);
        for (String whiteListUrl : whiteListUrlArr) {
            if (requestUrl.startsWith(whiteListUrl)) {
                return true;
            }
        }

        Object userIdObj = WebUtils.getSessionAttribute(request, CommonCnst.USER_ID);
        if (null == userIdObj) {
            UserOpenidVo userOpenidVo = new UserOpenidVo();
            Object obj = WebUtils.getSessionAttribute(request, OauthConst.OPENID);
            logger.error("openId:" + obj == null ? "null" : obj.toString());
            Assert.notNull(obj, "OPENID should not be null");
            userOpenidVo = userOpenidVo.selectOne(new EntityWrapper<UserOpenidVo>().eq("openid", String.valueOf(obj)));

            if (userOpenidVo == null || StringUtils.isEmpty(userOpenidVo.getId())) {
                //重定向到注册绑定界面
                if (CommonUtil.isAjaxRequest(request)) {
                    response.setStatus(CommonCnst.AJAX_BIND_HTTP_CODE);
                } else {
                    //这里构造的时候，需要加上转发的前缀
//                    String loginredurl = URLEncoder.encode(request.getRequestURL() + "?" + queryStr, "utf-8");
                    String loginredurl = URLEncoder.encode(makeRequestURL(request, "a"), "utf-8");
                    logger.info("######### initUser redirect : {}", this.bindUserUrl + "?bindredurl=" + loginredurl);
                    response.sendRedirect(this.bindUserUrl + "?bindredurl=" + loginredurl);
                }
                return false;
            } else {
                SysUser sysUser = new SysUser();
                sysUser = sysUser.selectOne(new EntityWrapper<SysUser>().eq("id", userOpenidVo.getId()));
                WebUtils.setSessionAttribute(request, CommonCnst.USER_ID, userOpenidVo.getId());
                WebUtils.setSessionAttribute(request, CommonCnst.USER_LOGIN_NAME, userOpenidVo.getLoginName());
                WebUtils.setSessionAttribute(request, CommonCnst.USER_INFO, sysUser);
                return true;
            }
        }
        return true;
    }

    @Override
    public void destroy() {
    }

    private String makeRequestURL(HttpServletRequest request, String pre) {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/" + pre;
        return basePath + request.getRequestURI() + "?" + request.getQueryString();
    }


}
