package com.yioa.ssm.service;

import com.yioa.ssm.domain.FlowHandlerVo;

/**
 * Created by tao on 2017-06-05.
 */
public interface ICustomerHandler {

    public FlowHandlerVo customerHandler(String flowStage,String orderId);
}
