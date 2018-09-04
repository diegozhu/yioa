package com.xseed.sms.service.impl;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.xseed.sms.common.SmsEvent;
import com.xseed.sms.common.TelecomSmsUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 使用电信接口的实现方式
 * Created by tao on 2017-07-28.
 */
@Service
public class TelecomSmsSendService implements InitializingBean {

    @Autowired
    private EventBus eventBus;

    @Subscribe
    public void sendSms(SmsEvent smsEvent) {
        TelecomSmsUtil.sendSms(smsEvent.getNum(), smsEvent.getMsg());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        eventBus.register(this);
    }
}
