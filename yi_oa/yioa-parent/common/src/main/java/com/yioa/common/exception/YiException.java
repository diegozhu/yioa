package com.yioa.common.exception;

/**
 * Created by tao on 2017-05-22.
 */
public class YiException extends Exception {

    private String errorCode;

    private String handleMsg;

    public YiException(String errorCode, String handleMsg) {
        super();
        this.errorCode = errorCode;
        this.handleMsg = handleMsg;
    }


    public YiException(String message, String errorCode, String handleMsg) {
        super(message);
        this.errorCode = errorCode;
        this.handleMsg = handleMsg;
    }

    public YiException(String message, Throwable cause, String errorCode, String handleMsg) {
        super(message, cause);
        this.errorCode = errorCode;
        this.handleMsg = handleMsg;
    }

    public YiException(Throwable cause, String errorCode, String handleMsg) {
        super(cause);
        this.errorCode = errorCode;
        this.handleMsg = handleMsg;
    }

    public YiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String errorCode, String handleMsg) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
        this.handleMsg = handleMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getHandleMsg() {
        return handleMsg;
    }

    public void setHandleMsg(String handleMsg) {
        this.handleMsg = handleMsg;
    }
}
