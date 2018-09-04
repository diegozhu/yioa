package com.yioa.test;

/**
 * Created by tao on 2017-05-25.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yioa.car.domain.CarSendOrderVo;
import com.yioa.car.mapper.CarSendOrderMapper;
import com.yioa.car.service.CarSendService;
import com.yioa.common.util.CommonUtil;
import com.yioa.core.CoreApplication;
import com.yioa.ssm.domain.FlowInfoVo;
import com.yioa.ssm.mapper.FlowInfoMapper;
import com.yioa.ssm.service.FlowService;
import com.yioa.ssm.util.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CoreApplication.class)
public class CarFlowApplicationTest {


    private static final String rootUrl = "http://127.0.0.1:8080";


    @Autowired
    private CarSendService carSendService;


    @Autowired
    private FlowService flowService;

    @Autowired
    private FlowInfoMapper flowInfoMapper;

    @Autowired
    private CarSendOrderMapper carSendOrderMapper;


    private static final Logger LOGGER = LoggerFactory.getLogger(CarFlowApplicationTest.class);

    private static ObjectMapper mapper = new ObjectMapper();

    private static String testOrderId = CommonUtil.getUUID();


    @Test
//    @Rollback
    public void flowtestAll() throws Exception {

        CarSendOrderVo carSendOrderVo = new CarSendOrderVo();

        //FIXME 测试的时候才需要这么干
        carSendOrderVo.setCarSendOrderId(testOrderId);

        carSendOrderVo.setFeedbackId("1");
        carSendOrderVo.setFeedbackName("thinkgem");
        carSendOrderVo.setCarSendOrderCode("xxxxxxxxx-order");
        carSendOrderVo.setCarId("123");
        carSendOrderVo.setCarNo("苏A-D00002");
        carSendOrderVo.setCarSendOrderContent("派车测试，看看能不能发车");
        carSendOrderVo.setDestination("南京万达");
        carSendOrderVo.setPassengerId("2");
        carSendOrderVo.setPassengerName("2hao乘客");
        carSendOrderVo.setDriverId("3");
        carSendOrderVo.setDriverName("3号司机");
        carSendOrderVo.setPlanBackDate(new Date());
        carSendOrderVo.setPlanLeaveDate(new Date());

        String reStr = this.carSendService.orderSubmit("1", "thinkgem", carSendOrderVo);
        Assert.assertEquals("1",reStr);

        CarSendOrderVo ww1 = this.carSendOrderMapper.selectById(testOrderId);
        FlowInfoVo ff = this.flowInfoMapper.selectById(ww1.getFlowInfoId());
        Assert.assertEquals(CarFlows.CAR_FLOW_AUDIT.name(),ff.getFlowStage());
        Assert.assertEquals(CarFlows.CAR_FLOW_AUDIT.name(),ww1.getFlowStage());




        CarSendOrderVo tVo = this.carSendOrderMapper.selectById(testOrderId);
        FlowInfoVo flowInfoVo = new FlowInfoVo();
        flowInfoVo.setFlowInfoId(tVo.getFlowInfoId());

        flowInfoVo.setNotes("审核通过");
        flowInfoVo.setAttachFile("xxxxxxxxxxxx 审核通过");

        String str = flowService.flowSubmit("1", "thinkgem",testOrderId, tVo.getFlowStage(), CarEvents.CAR_AUDIT_PASS.name(), null,flowInfoVo,FlowUtil.ORDER_CAR);
        Assert.assertEquals("1",str);
        CarSendOrderVo w1 = this.carSendOrderMapper.selectById(testOrderId);
        FlowInfoVo f1 = this.flowInfoMapper.selectById(w1.getFlowInfoId());
        Assert.assertEquals(CarFlows.CAR_FLOW_COMPLETE.name(),f1.getFlowStage());
        Assert.assertEquals(CarFlows.CAR_FLOW_COMPLETE.name(),w1.getFlowStage());



        flowInfoVo.setFlowInfoId(f1.getFlowInfoId());
        flowInfoVo.setNotes("让你回单");
        flowInfoVo.setAttachFile("xxxxxxxxxxxx回单");
        str = flowService.flowSubmit("1", "thinkgem",testOrderId, w1.getFlowStage(),CarEvents.CAR_COMPLETE.name(), null,flowInfoVo,FlowUtil.ORDER_CAR);
        Assert.assertEquals("1",str);
        CarSendOrderVo w2 = this.carSendOrderMapper.selectById(testOrderId);
        Assert.assertEquals(OaFlows.FLOW_DONE.name(),w2.getFlowStage());


//        flowInfoVo.setFlowInfoId(f2.getFlowInfoId());
//        flowInfoVo.setNotes("同意你归档");
//        flowInfoVo.setAttachFile("xxxxxxxxxxxx同意你归档");
//        str = flowService.flowSubmit("1", "thinkgem",testOrderId, w2.getFlowStage(),OaEvents.MILESTONE.name(), null,flowInfoVo);
//        Assert.assertEquals("1",str);
//        CarSendOrderVo w3 = this.carSendOrderMapper.selectById(testOrderId);
//        Assert.assertEquals(OaFlows.FLOW_DONE.name(),w3.getFlowStage());



    }





}
