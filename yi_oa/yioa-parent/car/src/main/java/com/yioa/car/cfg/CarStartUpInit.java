package com.yioa.car.cfg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.yioa.car.domain.CarSendOrderVo;
import com.yioa.car.mapper.CarSendOrderMapper;
import com.yioa.ssm.domain.FlowHandlerVo;
import com.yioa.ssm.service.HandlerService;
import com.yioa.ssm.service.ICustomerHandler;
import com.yioa.ssm.util.CarFlows;
import com.yioa.ssm.util.FlowUtil;

/**
 * Created by tao on 2017-06-05.
 */
@Component
public class CarStartUpInit {

    @Autowired
    private HandlerService handlerService;

    @Autowired
    private CarSendOrderMapper carSendOrderMapper;

    @PostConstruct
    public void init(){
        handlerService.addCustomerHandler(CarFlows.CAR_FLOW_DRIVER_COMPLETE.name(), new ICustomerHandler() {
            @Override
            public FlowHandlerVo customerHandler(String flowStage, String orderId) {

                Assert.notNull(flowStage,"flowStage should not be null");
                Assert.notNull(orderId,"orderId should not be null");

                CarSendOrderVo vo = carSendOrderMapper.selectById(orderId);

                //FlowHandlerVo flowHandlerVo = new FlowHandlerVo(vo.getFeedbackName(),vo.getFeedbackId(), FlowUtil.USER_HANDLER_TYPE);
                FlowHandlerVo flowHandlerVo = new FlowHandlerVo(vo.getDriverName(),vo.getDriverId(), FlowUtil.USER_HANDLER_TYPE);
                return flowHandlerVo;
            }
        });
        
        handlerService.addCustomerHandler(CarFlows.CAR_FLOW_COMPLETE.name(), new ICustomerHandler() {
            @Override
            public FlowHandlerVo customerHandler(String flowStage, String orderId) {

                Assert.notNull(flowStage,"flowStage should not be null");
                Assert.notNull(orderId,"orderId should not be null");

                CarSendOrderVo vo = carSendOrderMapper.selectById(orderId);

                FlowHandlerVo flowHandlerVo = new FlowHandlerVo(vo.getFeedbackName(),vo.getFeedbackId(), FlowUtil.USER_HANDLER_TYPE);
                return flowHandlerVo;
            }
        });
    }




}
