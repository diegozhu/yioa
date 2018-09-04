package com.yioa.common.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.web.util.WebUtils;

import com.yioa.common.exception.YiException;

/**
 * Created by tao on 2017-05-29.
 */
public final class CommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

    public static final int HASH_INTERATIONS = 1024;
    public static final String UTF8 = "UTF-8";


    private CommonUtil() {
    }


    public static final String getUUID() {

        UUID uuid = UUID.randomUUID();

        return StringUtils.replaceAll(String.valueOf(uuid), "-", "");
    }


    public static boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(header) ? true : false;
        return isAjax;
    }

    public static String getUserIdFromSession(HttpServletRequest servletRequest) throws YiException {

        Object val = WebUtils.getSessionAttribute(servletRequest, CommonCnst.USER_ID);
        if (null == val) {
            throw new YiException(SysErrorCnst.SESSION_EXPIRE, SysErrorCnst.MSG_SESSION_EXPIRE);
        }
        return String.valueOf(val);
    }

    public static String getLoginStateFromSession(HttpServletRequest servletRequest) {
        Object val = WebUtils.getSessionAttribute(servletRequest, CommonCnst.USER_ID);
        if(null == val){
            return "111";
        }
        return "222";
    }

    public static String getUserLoginNameFromSession(HttpServletRequest servletRequest) throws YiException {

        Object val = WebUtils.getSessionAttribute(servletRequest, CommonCnst.USER_LOGIN_NAME);
        if (null == val) {
            throw new YiException(SysErrorCnst.SESSION_EXPIRE, SysErrorCnst.MSG_SESSION_EXPIRE);
        }
        return String.valueOf(val);
    }
    
    public static <T> T getUserLoginInfoFromSession(HttpServletRequest servletRequest) throws YiException {

        Object val = WebUtils.getSessionAttribute(servletRequest, CommonCnst.USER_INFO);
        if (null == val) {
            throw new YiException(SysErrorCnst.SESSION_EXPIRE, SysErrorCnst.MSG_SESSION_EXPIRE);
        }
        return (T)val;
    }


    public static String getFullUrl(HttpServletRequest request) {
        String queryStr = request.getQueryString();
        String url = request.getRequestURL() + "?" + queryStr;
        return url;
    }

    public static synchronized String genOrderCode(String pre) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error(e.toString(), e);
        }
        String dateStr = sdf.format(new Date());
        return pre + dateStr;
    }


    public static String getFileFolderName(String loginName) {
        return "up_"+RSHash(loginName);// % 100;
    }

    public static String genFileName() throws UnsupportedEncodingException {
//        return DigestUtils.md5Hex(UUID.randomUUID().toString());

        return DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes(UTF8));
    }

    public static int RSHash(String str) {
        int b = 378551;
        int a = 63689;
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = hash * a + str.charAt(i);
            a = a * b;
        }
        return (hash & 0x7FFFFFFF);
    }

    public static void setExIdStr(String exStr, Map<String, Object> tMap) {
        if (StringUtils.isNotEmpty(exStr)) {
            String[] exIdArr = StringUtils.split(exStr, ",");
            StringBuffer sb = new StringBuffer("'");
            sb.append(StringUtils.join(exIdArr, "','")).append("'");
            tMap.put("idStr", sb.toString());

            tMap.put("idList", Arrays.asList(exIdArr));
        }
    }

    /**
     * 验证密码
     * @param plainPassword 明文密码
     * @param password 密文密码
     * @return 验证成功返回true
     */
    public static boolean validatePassword(String plainPassword, String password) {
        String plain = Encodes.unescapeHtml(plainPassword);
        byte[] salt = Encodes.decodeHex(password.substring(0,16));
        byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
        return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
    }

}
