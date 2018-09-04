package com.yioa.oa.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.ImmutableMap;
import com.xseed.sms.service.SmsService;
import com.xseed.ssmflow.cfg.service.IPersistCallBackService;
import com.xseed.ssmflow.cfg.util.SsmFlowCnst;
import com.yioa.car.domain.CarSendOrderVo;
import com.yioa.car.mapper.CarSendOrderMapper;
import com.yioa.common.util.CommonUtil;
import com.yioa.common.util.SysErrorCnst;
import com.yioa.market.domain.MarketOrderVo;
import com.yioa.market.mapper.MarketOrderMapper;
import com.yioa.oa.domain.WorkOrderVo;
import com.yioa.oa.mapper.WorkOrderMapper;
import com.yioa.panel.domain.PanelVo;
import com.yioa.panel.mapper.PanelOrderMapper;
import com.yioa.ssm.domain.FlowCustParam;
import com.yioa.ssm.domain.FlowHandlerVo;
import com.yioa.ssm.domain.FlowInfoVo;
import com.yioa.ssm.mapper.FlowInfoMapper;
import com.yioa.ssm.service.HandlerService;
import com.yioa.ssm.util.CarFlows;
import com.yioa.ssm.util.CarSendOrderStatus;
import com.yioa.ssm.util.FlowUtil;
import com.yioa.ssm.util.MarketFlows;
import com.yioa.ssm.util.OaFlows;
import com.yioa.ssm.util.PanelFlows;
import com.yioa.sys.domain.SysUser;
import com.yioa.sys.mapper.SysUserMapper;

/**
 * 流程流转成功之后，需要进行的回调，主要是一系列的update
 * Created by tao on 2017-05-30.
 */

@Service
public class FlowPersistCallBackService implements IPersistCallBackService {

    private Logger logger = LoggerFactory.getLogger(FlowPersistCallBackService.class);


    @Autowired
    private WorkOrderMapper workOrderMapper;

    @Autowired
    private CarSendOrderMapper carSendOrderMapper;

    @Autowired
    private PanelOrderMapper panelOrderMapper;

    @Autowired
    private MarketOrderMapper marketOrderMapper;

    @Autowired
    private SysUserMapper sysUserMapper;


    @Autowired
    private FlowInfoMapper flowInfoMapper;

    @Autowired
    private HandlerService handlerService;

    @Autowired
    private SmsService smsService;


    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM月dd日HH:mm");


    private void handlerOaOrderAtEnd(State<String, String> state, FlowCustParam flowCustParam) {

        String workOrderId = flowCustParam.getWorkOrderId();
        WorkOrderVo workOrderVo = workOrderMapper.selectById(workOrderId);

        //更新环节信息
        workOrderVo.setFlowStage(state.getId());
        workOrderVo.setFlowStageName(OaFlows.valueOf(workOrderVo.getFlowStage()).getName());
//        workOrderVo.setStatus(WorkOrderStatus.STATUS_DONE.name());
        workOrderVo.setStatus(OaFlows.valueOf(workOrderVo.getFlowStage()).getStatus());
        workOrderVo.updateById();

    }

    private void handlerCarOrderAtEnd(State<String, String> state, FlowCustParam flowCustParam) {

        String orderId = flowCustParam.getWorkOrderId();
        CarSendOrderVo carSendOrderVo = carSendOrderMapper.selectById(orderId);

        //更新环节信息
        carSendOrderVo.setFlowStage(state.getId());
        carSendOrderVo.setStatus(CarSendOrderStatus.STATUS_DONE.getName());
        carSendOrderVo.updateById();

    }
    private void handlerPanelOrderAtEnd(State<String, String> state,FlowCustParam flowCustParam) {
        String panelOrderId = flowCustParam.getWorkOrderId();
        PanelVo panelVo = panelOrderMapper.selectById(panelOrderId);
        //更新环节信息
        panelVo.setFlowStage(state.getId());
        panelVo.setFlowStageName(PanelFlows.valueOf(panelVo.getFlowStage()).getName());
        panelVo.setStatus(PanelFlows.valueOf(panelVo.getFlowStage()).getStatus());
        panelVo.updateById();
    }

    private void handlerMarketOrderAtEnd(State<String,String> state,FlowCustParam flowCustParam) {
        String marketOrderId = flowCustParam.getWorkOrderId();
        MarketOrderVo marketOrderVo = marketOrderMapper.selectById(marketOrderId);
        //更新环节信息
        marketOrderVo.setFlowStage(state.getId());
        marketOrderVo.setFlowStageName(MarketFlows.valueOf(marketOrderVo.getFlowStage()).getName());
        marketOrderVo.setStatus(MarketFlows.valueOf(marketOrderVo.getFlowStage()).getStatus());
        marketOrderVo.updateById();
    }



    private void handlerOaOrder(State<String, String> state, FlowCustParam flowCustParam, FlowInfoVo flowInfoVo) {

        String workOrderId = flowCustParam.getWorkOrderId();
        WorkOrderVo workOrderVo = workOrderMapper.selectById(workOrderId);

        //更新环节信息
        workOrderVo.setFlowStage(state.getId());
        workOrderVo.setFlowStageName(OaFlows.valueOf(workOrderVo.getFlowStage()).getName());
        workOrderVo.setStatus(OaFlows.valueOf(workOrderVo.getFlowStage()).getStatus());

        //当前环节
        workOrderVo.setFlowInfoId(flowInfoVo.getFlowInfoId());
        workOrderVo.updateById();

    }

    private void handlerCarOrder(State<String, String> state, FlowCustParam flowCustParam, FlowInfoVo flowInfoVo) {

        String workOrderId = flowCustParam.getWorkOrderId();
        CarSendOrderVo carSendOrderVo = this.carSendOrderMapper.selectById(workOrderId);

        //更新环节信息
        carSendOrderVo.setFlowStage(state.getId());

        //更新环节信息
        carSendOrderVo.setFlowStageName(CarFlows.valueOf(carSendOrderVo.getFlowStage()).getName());
        carSendOrderVo.setStatus(CarFlows.valueOf(carSendOrderVo.getFlowStage()).getStatus());

        //当前环节
        carSendOrderVo.setFlowInfoId(flowInfoVo.getFlowInfoId());
        carSendOrderVo.updateById();
    }

    private void handlerPanelOrder(State<String, String> state,FlowCustParam flowCustParam,FlowInfoVo flowInfoVo) {
        String workOrderId = flowCustParam.getWorkOrderId();
        PanelVo panelVo = this.panelOrderMapper.selectById(workOrderId);

        //更新环节信息
        panelVo.setFlowStage(state.getId());

        //更新环节信息
        panelVo.setFlowStageName(PanelFlows.valueOf(panelVo.getFlowStage()).getName());
        panelVo.setStatus(PanelFlows.valueOf(panelVo.getFlowStage()).getStatus());

        //当前环节
        panelVo.setFlowInfoId(flowInfoVo.getFlowInfoId());
        panelVo.updateById();

    }

    private void handlerMarketOrder(State<String,String> state,FlowCustParam flowCustParam,FlowInfoVo flowInfoVo) {
        String workOrderId = flowCustParam.getWorkOrderId();
        MarketOrderVo marketOrderVo = this.marketOrderMapper.selectById(workOrderId);

        //更新环节信息
        marketOrderVo.setFlowStage(state.getId());
        marketOrderVo.setFlowStageName(MarketFlows.valueOf(marketOrderVo.getFlowStage()).getName());
        marketOrderVo.setStatus(MarketFlows.valueOf(marketOrderVo.getFlowStage()).getStatus());

        //当前环节
        marketOrderVo.setFlowInfoId(flowInfoVo.getFlowInfoId());
        marketOrderVo.updateById();
    }


    /**
     * 环节流转成功，回调函数中处理业务逻辑
     * 1 插入新环节信息
     * 2 修改旧环节信息
     *
     * @param state
     * @param message
     * @param transition
     * @param stateMachine
     */
    @Override
    public void callBack(State<String, String> state, Message<String> message, Transition<String, String> transition, StateMachine<String, String> stateMachine) {

        if (message != null && message.getHeaders().containsKey(SsmFlowCnst.FLOW_CUST_PARAM)) {

            logger.info("########### : {} ", state);
            FlowCustParam flowCustParam = message.getHeaders().get(SsmFlowCnst.FLOW_CUST_PARAM, FlowCustParam.class);

            //*******************************************修改前一环节处理人信息，以及处理时间
            if (StringUtils.isNotEmpty(flowCustParam.getPreFlowInfoId())) {
                FlowInfoVo preFlowInfo = flowInfoMapper.selectById(flowCustParam.getPreFlowInfoId());
                Assert.notNull(preFlowInfo, "preFlowInfo should not be null: " + preFlowInfo);
                preFlowInfo.setCompleteDate(new Date());
                preFlowInfo.setCompleteUserName(flowCustParam.getHandleBy());
                preFlowInfo.setCompleteUserId(flowCustParam.getHandleById());
                preFlowInfo.setNotes(flowCustParam.getNotes());
                preFlowInfo.setAttachFile(flowCustParam.getAttachFile());
                preFlowInfo.updateById();
            }

            //*********************************************如果流程结束了，就走这段分支
            logger.info("##########current state is {} and stateMachine.isComplete() : {}", state.getId(), stateMachine.isComplete());
            if (stateMachine.isComplete() || state.getId().equalsIgnoreCase(OaFlows.FLOW_DONE.name())) {
                //*******************************************处理工单信息
                if (flowCustParam.getOrderType().equalsIgnoreCase(FlowUtil.ORDER_CAR)) {
                    this.handlerCarOrderAtEnd(state, flowCustParam);
                }else if (flowCustParam.getOrderType().equalsIgnoreCase(FlowUtil.ORDER_PANEL)){
                    this.handlerPanelOrderAtEnd(state, flowCustParam);
                } else if(flowCustParam.getOrderType().equalsIgnoreCase(FlowUtil.ORDER_MARKET)){
                    this.handlerMarketOrderAtEnd(state,flowCustParam);
                } else {
                    this.handlerOaOrderAtEnd(state, flowCustParam);
                }
                return;
            }


            //*******************************************插入新流程数据修改处理时间，环节等工单信息
            FlowInfoVo flowInfoVo = new FlowInfoVo();

            flowInfoVo.setFlowInfoId(CommonUtil.getUUID());
            flowInfoVo.setWorkOrderId(flowCustParam.getWorkOrderId());
            flowInfoVo.setPrevFlowInfoId(flowCustParam.getPreFlowInfoId());
            flowInfoVo.setCreateDate(new Date());


            //当前环节
            flowInfoVo.setFlowStage(state.getId());
            if (flowCustParam.getOrderType().equalsIgnoreCase(FlowUtil.ORDER_CAR)) {
                flowInfoVo.setFlowStageName(CarFlows.valueOf(state.getId()).getName());
            } else if(flowCustParam.getOrderType().equalsIgnoreCase(FlowUtil.ORDER_PANEL)) {
                flowInfoVo.setFlowStageName(PanelFlows.valueOf(state.getId()).getName());
            } else if(flowCustParam.getOrderType().equalsIgnoreCase(FlowUtil.ORDER_MARKET)){
                flowInfoVo.setFlowStageName(MarketFlows.valueOf(state.getId()).getName());
            } else {
                flowInfoVo.setFlowStageName(OaFlows.valueOf(state.getId()).getName());
            }
            flowInfoVo.insert();

            //当前环节处理人
            FlowHandlerVo flowHandlerVo = flowCustParam.getFlowHandlerVo();
            if (null == flowHandlerVo) {
//                flowHandlerVo = HandlerService.getDefaultFlowHandler();
                flowHandlerVo = handlerService.getFlowHandler(flowInfoVo.getFlowStage(), flowCustParam.getWorkOrderId());
            }
            flowInfoVo.setHandleBy(flowHandlerVo.getHandlerBy());
            flowInfoVo.setHandleById(flowHandlerVo.getHandlerById());
            flowInfoVo.setHandleByType(flowHandlerVo.getHandlerByType());

            flowInfoVo.updateById();
            //增加回调通知
            try {
                this.sendSms(flowInfoVo.getCreateDate(), flowCustParam.getOrderType(),flowCustParam.getHandleById(), flowInfoVo);
            } catch (Exception e) {
                logger.error(e.toString(), e);
            }


            //*******************************************处理工单信息，主要是更新当前的环节
            if (flowCustParam.getOrderType().equalsIgnoreCase(FlowUtil.ORDER_CAR)) {
                this.handlerCarOrder(state, flowCustParam, flowInfoVo);
            } else if(flowCustParam.getOrderType().equalsIgnoreCase(FlowUtil.ORDER_PANEL)){
                this.handlerPanelOrder(state, flowCustParam, flowInfoVo);
            } else if(flowCustParam.getOrderType().equalsIgnoreCase(FlowUtil.ORDER_MARKET)){
                this.handlerMarketOrder(state,flowCustParam,flowInfoVo);
            } else {
                this.handlerOaOrder(state, flowCustParam, flowInfoVo);
            }
        }
    }

    /**
     * 实现的不优雅，但是简单，看后面的复杂度是否增加，再决定要不要扩展
     */
    private void sendSms(Date createDate, String orderType,String handlerById, FlowInfoVo flowInfoVo) {
        String mobileStr = null;
        SysUser handlerByUser = this.sysUserMapper.selectById(handlerById);
        String handlerByName = handlerByUser.getName();

        if (flowInfoVo.getHandleByType().equalsIgnoreCase(FlowUtil.USER_HANDLER_TYPE)) {

            SysUser sysUser = this.sysUserMapper.selectById(flowInfoVo.getHandleById());
            Assert.notNull(sysUser, "user should not ne null, " + sysUser);
            Assert.notNull(sysUser.getMobile(), "mobile should not ne null, " + sysUser.getMobile());
            mobileStr = sysUser.getMobile();
        } else if (flowInfoVo.getHandleByType().equalsIgnoreCase(FlowUtil.ROLE_HANDLER_TYPE)) {

            logger.info("######## we will  send sms to all handler on 'role' ");
            Map<String,Object> tMap = ImmutableMap.of("roleId",flowInfoVo.getHandleById());
            List<SysUser> userList = this.sysUserMapper.queryUserByRoleId(tMap);

            mobileStr = userList.stream().filter(user -> StringUtils.isNotEmpty(user.getMobile())).map(user -> user.getMobile()).collect(Collectors.joining(","));
//            numList = userList.stream().map(user -> user.getMobile()).collect(Collectors.toList());
        } else {
//            throw  new YiException(SysErrorCnst.FLOW_HANDLER_ORG_ERR,SysErrorCnst.MSG_FLOW_HANDLER_ORG_ERR);
            logger.error("############## : {}", SysErrorCnst.MSG_FLOW_HANDLER_ORG_ERR);
        }

        LocalDateTime localDateTime = LocalDateTime.ofInstant(createDate.toInstant(), ZoneId.systemDefault());

        if (StringUtils.isBlank(mobileStr)) {
        	return ;
        }
        String title = "智慧派单";
        String subject = null;
        if(orderType.equalsIgnoreCase(FlowUtil.ORDER_CAR)) {
            CarSendOrderVo workOrderVo = carSendOrderMapper.selectById(flowInfoVo.getWorkOrderId());
            title = "智慧派车";
            subject = workOrderVo.getCarSendOrderSubject();
        } else if(orderType.equalsIgnoreCase(FlowUtil.ORDER_MARKET)) {
            MarketOrderVo workOrderVo = marketOrderMapper.selectById(flowInfoVo.getWorkOrderId());
            subject = workOrderVo.getWork_order_subject();
        } else if(orderType.equalsIgnoreCase(FlowUtil.ORDER_OA)) {
            WorkOrderVo workOrderVo = workOrderMapper.selectById(flowInfoVo.getWorkOrderId());
            subject = workOrderVo.getWorkOrderSubject();
        }else {
        	
        }
        smsService.sendNotice(dtf.format(localDateTime), title, handlerByName, subject, mobileStr);
    }


}
