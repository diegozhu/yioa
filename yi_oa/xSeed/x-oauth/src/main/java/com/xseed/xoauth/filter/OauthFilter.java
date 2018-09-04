package com.xseed.xoauth.filter;

/**
 * Created by tao on 2017-04-29.
 */

import com.xseed.xoauth.util.OauthConst;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OauthFilter implements Filter {


    private final static Logger logger = LoggerFactory.getLogger(OauthFilter.class);
    /**
     * 系统地址
     */
    private String sysTokenPath;

    /**
     * 易信地址
     */
    private String codePath;

    private String[] whiteListUrlArr;

    public String[] getWhiteListUrlArr() {
        return whiteListUrlArr;
    }

    public void setWhiteListUrlArr(String[] whiteListUrlArr) {
        this.whiteListUrlArr = whiteListUrlArr;
    }

    public String getCodePath() {
        return codePath;
    }

    public void setCodePath(String codePath) {
        this.codePath = codePath;
    }

    public String getSysTokenPath() {
        return sysTokenPath;
    }

    public void setSysTokenPath(String sysTokenPath) {
        this.sysTokenPath = sysTokenPath;
    }

    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
        logger.info("########### OauthFilter inited");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;

        boolean bool = checkOpenid(req, res);
        if (bool) {
            chain.doFilter(req, res);
        }

    }

    private boolean checkOpenid(ServletRequest req, ServletResponse res) throws IOException {

        //首先尝试从session中获取登陆成功的标识，如果不存在，认为没有登陆

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String requestUrl = request.getRequestURI();
        request.getQueryString();
        logger.info("#######requestUrl: " + requestUrl);
        logger.info("#######whiteListUrlArr: " + StringUtils.join(whiteListUrlArr, ","));
        logger.info("#######getSession end");
        Object obj = WebUtils.getSessionAttribute(request, OauthConst.OPENID);
        if (null == obj) {
            if (this.isAjaxRequest(request)) {
                response.setStatus(999);
                return false;
            }

            //未登陆，重定向到易信的认证界面
            for (String whiteListUrl : whiteListUrlArr) {
                if (requestUrl.startsWith(whiteListUrl)) {
                    return true;
                }
            }
            //requestUrl 前面需要加上Ngnix转发的前缀
            if (requestUrl.startsWith("/")) {
                requestUrl = "/a" + requestUrl;
            } else {
                requestUrl = "/a/" + requestUrl;
            }
            String queryStr = request.getQueryString();
            requestUrl = URLEncoder.encode(requestUrl + "?" + queryStr, OauthConst.UTF8);

            String redurl = sysTokenPath + "&redurl=" + requestUrl;
            redurl = URLEncoder.encode(redurl, OauthConst.UTF8);
            String tagUrl = StringUtils.replaceAll(codePath, "REDIRECT_URI", redurl);
            response.sendRedirect(tagUrl);
            return false;
        }
        return true;
    }

    @Override
    public void destroy() {
    }

    private void handlderWhenAjax() {
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(header) ? true : false;
        return isAjax;
    }

    private String getFullUrl(HttpServletRequest request) {
        String queryStr = request.getQueryString();
        String url = request.getRequestURL() + "?" + queryStr;
        return url;
    }


}