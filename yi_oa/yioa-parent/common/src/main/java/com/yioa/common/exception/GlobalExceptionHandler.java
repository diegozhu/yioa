package com.yioa.common.exception;

import com.yioa.common.util.CommonCnst;
import com.yioa.common.util.CommonUtil;
import com.yioa.common.util.SysErrorCnst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by tao on 2017-05-22.
 */
@ControllerAdvice
class GlobalExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "error";

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = YiException.class)
    @ResponseBody
    public ErrorInfo<YiException> jsonErrorHandler(HttpServletRequest req, HttpServletResponse response, YiException e) throws Exception {

        logger.error(e.getMessage(),e);

        if(CommonUtil.isAjaxRequest(req)){
            response.setStatus(CommonCnst.AJAX_ERROR);
            throw new Exception(e.getMessage());
        }

        ErrorInfo<YiException> r = new ErrorInfo<>();
        r.setMessage(e.getMessage());
        r.setErrorCode(e.getErrorCode());
        r.setHandleMsg(e.getHandleMsg());
        r.setData(e);
        r.setUrl(req.getRequestURL().toString());



        if(e.getErrorCode().equalsIgnoreCase(SysErrorCnst.SESSION_EXPIRE)){
            if(CommonUtil.isAjaxRequest(req)){
                response.setStatus(CommonCnst.AJAX_LOGIN_HTTP_CODE);
            }else{
                //这个情况比较少，暂时先空着，应该是重定向到登陆界面
                response.sendRedirect("");
            }
        }

        return r;
    }


}
