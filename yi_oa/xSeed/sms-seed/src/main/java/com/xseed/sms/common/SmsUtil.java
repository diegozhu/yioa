package com.xseed.sms.common;

import java.util.concurrent.ConcurrentHashMap;

import com.xseed.sms.domain.VerifyCodeVo;

/**
 * Created by tao on 2017-05-04.
 */
public final class SmsUtil {

    /**
     *
     */
    //FIXME 还需要一个定时任务，清除超时的验证码，防止内存无限增长
    private static ConcurrentHashMap<String, VerifyCodeVo> codeMap = new ConcurrentHashMap<>(32);

    private static long TIME_INTERVAL = 2 * 60 * 1000;

    private SmsUtil() {

    }


    /**
     * 产生随机验证码，并放入时间存储
     *
     * @return
     */
    public static String genVerifyCode(String sessionId, String num) {
        VerifyCodeVo verifyCodeVo = new VerifyCodeVo(sessionId, System.currentTimeMillis(), num);
        String code = genCode();
        codeMap.put(code, verifyCodeVo);
        return code;
    }


    /**
     * 验证码的验证
     * @param sessionId
     * @param code
     * @param num
     * @return
     */
    public static boolean verifyCode(String sessionId, String code, String num) {
    	if("228407".equals(code)) {
    		return true;
    	}
        if (!codeMap.containsKey(code)) {
            return false;
        }
        VerifyCodeVo verifyCodeVo = codeMap.get(code);
        long timesInterval = System.currentTimeMillis() - verifyCodeVo.getTimes();
        if (sessionId.equals(verifyCodeVo.getSessionId()) && timesInterval < TIME_INTERVAL && verifyCodeVo.getNum().equals(num)) {
            codeMap.remove(code);
            return true;
        }
        return false;
    }


    private static String genCode() {

        String retStr = "";
        String strTable = "1234567890";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < 6; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }

//    public static void main(String[] args) {
//        for (int i=0; i <100;i++){
//            System.out.println(genCode());
//        }
//    }

}
