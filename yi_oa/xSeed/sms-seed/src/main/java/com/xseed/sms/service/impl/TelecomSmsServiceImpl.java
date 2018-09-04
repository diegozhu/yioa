package com.xseed.sms.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.xseed.sms.common.SmsEvent;
import com.xseed.sms.common.SmsUtil;
import com.xseed.sms.service.SmsService;

/**
 * 基于电信短信接口的实现。
 * <p>
 * Created by tao on 2017-05-03.
 */
@Service
public class TelecomSmsServiceImpl implements SmsService {

    private final static Logger logger = LoggerFactory.getLogger(SmsService.class);

    @Autowired
    private EventBus eventBus;


    @Override
    public String sendWithName(String sessionId, String name, String num) {

        StringBuffer sb = new StringBuffer(name);
        sb.append("你好，你的验证码是");
        sb.append(SmsUtil.genVerifyCode(sessionId, num));
        sb.append("，请于2分钟内完整验证。");
        eventBus.post(new SmsEvent(num, sb.toString()));
        return "1";
    }


    @Override
    @Async
    public String sendNotice(String createDate, String orderType,String handlerByName, String subject, String num) {
    	logger.debug("");

        StringBuffer sb = new StringBuffer("您好！您在 ");
        sb.append(createDate);
        sb.append(" 收到一个来自于 <").append(handlerByName).append("> ");
        if(StringUtils.isNotBlank(subject)) {
        	sb.append("关于【" + subject + "】");
        }
        sb.append("的 <").append(orderType).append("> 任务，请及时处理。");

        eventBus.post(new SmsEvent(num, sb.toString()));
        return "1";

    }
    
    @Override
    @Async
    public String sendSms(String text, String num) {
        eventBus.post(new SmsEvent(num, text));
        return "1";

    }

    @Override
    @Async
    public String send(String sessionId, String num) {
        return this.sendWithName(sessionId, "用户", num);
    }


    /**
     * 验证通过返回1 ，否则返回0
     *
     * @param sessionId
     * @param code
     * @param num
     * @return
     */
    @Override
    public String valid(String sessionId, String code, String num) {
        boolean bool = SmsUtil.verifyCode(sessionId, code, num);
        return bool ? "1" : "0";
    }

}
