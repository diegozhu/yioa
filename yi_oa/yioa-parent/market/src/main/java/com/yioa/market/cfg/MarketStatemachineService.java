package com.yioa.market.cfg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import com.yioa.common.util.CommonUtil;
import com.yioa.market.cfg.MarketStateMachineConfig.MarketEvent;
import com.yioa.market.cfg.MarketStateMachineConfig.MarketState;
import com.yioa.market.domain.MarketOrderVo;
import com.yioa.market.mapper.MarketOrderMapper;
import com.yioa.ssm.domain.FlowInfoVo;
import com.yioa.ssm.mapper.FlowInfoMapper;
import com.yioa.sys.domain.SysUser;

@Component
@WithStateMachine(name= MarketStateMachineConfig.NAME, id = MarketStateMachineConfig.NAME)
public class MarketStatemachineService {
	
	@Autowired
	private MarketOrderMapper marketOrderMapper;
	
	@Autowired
	private FlowInfoMapper flowInfoMapper;

	@Autowired
	private StateMachine<MarketState, MarketEvent> stateMachine;
	
    private static final Logger logger = LoggerFactory.getLogger(MarketStatemachineService.class);

	
	public boolean sendEvent(MarketEvent event, MarketOrderVo order, SysUser currentUser, FlowInfoVo flowInfoVo) {
        synchronized (String.valueOf(order.getMarket_send_order_id()).intern()) {
            boolean success = false;
			System.out.println("id=" + order.getMarket_send_order_id() + " 状态机 orderStateMachine:" + stateMachine);
            try {
            	stateMachine.start();
                //尝试恢复状态机状态
                MarketState preState = restore(stateMachine, order, currentUser);
                System.out.println("id=" + order.getMarket_send_order_id() + " 状态机 orderStateMachine id=" + stateMachine.getId());
                //添加延迟用于线程安全测试
                //Thread.sleep(1000);
                success = stateMachine.sendEvent(MessageBuilder.withPayload(event).build());
                //持久化状态机状态
                if(success) {
                	persist(stateMachine, order, currentUser, preState, event, flowInfoVo);
                }
            } catch (Exception e) {
            	logger.error("error send event", e);
                e.printStackTrace();
            } finally {
            	stateMachine.stop();
            }
            return success;
        }
    }
	
	public List<MarketEvent> getAviableActions(MarketOrderVo order) {
		MarketState state = MarketState.valueOf(order.getStatus());
		ArrayList<MarketEvent> eventsForState = stateMachine.getTransitions().stream().filter(p -> p.getSource().getId().equals(state)).map(p -> p.getTrigger().getEvent()).collect(Collectors.toCollection(ArrayList::new));
		return eventsForState;
	} 
	
	public StateMachine<MarketState, MarketEvent> getStatemachine() {
		return stateMachine;
	}
	
	private MarketState restore(StateMachine<MarketState, MarketEvent> stateMachine, MarketOrderVo order, SysUser currentUser) {
		MarketState state = MarketState.valueOf(order.getStatus());
		List<StateMachineAccess<MarketState, MarketEvent>> withAllRegions = stateMachine.getStateMachineAccessor().withAllRegions();
        for (StateMachineAccess<MarketState, MarketEvent> a : withAllRegions) {
            a.resetStateMachine(new DefaultStateMachineContext<MarketState, MarketEvent>(state, null, null, null));
        }
        return state;
	}

	private void persist(StateMachine<MarketState, MarketEvent> stateMachine, MarketOrderVo order, SysUser user, MarketState preState, MarketEvent event, FlowInfoVo flowInfoVo) {
		MarketState state = stateMachine.getState().getId();
		order.setStatus(state.toString());
		marketOrderMapper.updateById(order);

		if(flowInfoVo == null) {
			flowInfoVo = new FlowInfoVo();
		}
        flowInfoVo.setFlowInfoId(CommonUtil.getUUID());
        flowInfoVo.setWorkOrderId(order.getMarket_send_order_id());
        flowInfoVo.setCreateDate(new Date());
        flowInfoVo.setNewStatus(state.toString());
        flowInfoVo.setPreStatus(preState.toString());
        flowInfoVo.setAction(event.toString());
        flowInfoVo.setCompleteUserId(user.getId());
        flowInfoVo.setCompleteUserName(user.getName());
		flowInfoMapper.insert(flowInfoVo);
		
		
		logger.debug("persis");
	}

}
