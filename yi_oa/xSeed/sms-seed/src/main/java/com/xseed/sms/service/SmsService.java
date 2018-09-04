package com.xseed.sms.service;


/**
 * Created by tao on 2017-05-03.
 */

public interface SmsService {

    /**
     * 发送短信验证码
     *
     * @param name
     * @param num
     * @return
     */
    public String sendWithName(String sessionId, String name, String num);

    /**
     * 发送短信通知
     *
     * @param createDate
     * @param orderType
     * @param mobileStr 
     * @return
     */
    public String sendNotice(String createDate, String orderType,String handlerByName,String subject, String mobileStr);

    /**
     * 只有号码的短信发送
     *
     * @param num
     * @return
     */
    public String send(String sessionId, String num);


    /**
     * 校验
     *
     * @param code
     * @param num
     * @return
     */
    public String valid(String sessionId, String code, String num);

	String sendSms(String text, String num);
}
