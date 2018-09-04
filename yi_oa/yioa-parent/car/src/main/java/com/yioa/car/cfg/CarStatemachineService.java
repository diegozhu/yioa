package com.yioa.car.cfg;

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

import com.yioa.car.cfg.CarStateMachineConfig.CarEvent;
import com.yioa.car.cfg.CarStateMachineConfig.CarState;
import com.yioa.car.domain.CarSendOrderVo;
import com.yioa.car.mapper.CarSendOrderMapper;
import com.yioa.common.util.CommonUtil;
import com.yioa.ssm.domain.FlowInfoVo;
import com.yioa.ssm.mapper.FlowInfoMapper;
import com.yioa.sys.domain.SysUser;

@Component
@WithStateMachine(name= CarStateMachineConfig.NAME, id = CarStateMachineConfig.NAME)
public class CarStatemachineService {
	
	@Autowired
	private CarSendOrderMapper carOrderMapper;
	
	@Autowired
	private FlowInfoMapper flowInfoMapper;

	@Autowired
	private StateMachine<CarState, CarEvent> stateMachine;
	
    private static final Logger logger = LoggerFactory.getLogger(CarStatemachineService.class);

	
	public boolean sendEvent(CarEvent event, CarSendOrderVo order, SysUser currentUser, FlowInfoVo flowInfoVo) {
        synchronized (String.valueOf(order.getCarSendOrderId()).intern()) {
            boolean success = false;
			System.out.println("id=" + order.getCarSendOrderId() + " 状态机 orderStateMachine:" + stateMachine);
            try {
            	stateMachine.start();
                //尝试恢复状态机状态
                CarState preState = restore(stateMachine, order, currentUser);
                System.out.println("id=" + order.getCarSendOrderId() + " 状态机 orderStateMachine id=" + stateMachine.getId());
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
	
	public List<CarEvent> getAviableActions(CarSendOrderVo order) {
		CarState state = CarState.valueOf(order.getStatus());
		ArrayList<CarEvent> eventsForState = stateMachine.getTransitions().stream().filter(p -> p.getSource().getId().equals(state)).map(p -> p.getTrigger().getEvent()).collect(Collectors.toCollection(ArrayList::new));
		return eventsForState;
	} 
	
	public StateMachine<CarState, CarEvent> getStatemachine() {
		return stateMachine;
	}
	
	private CarState restore(StateMachine<CarState, CarEvent> stateMachine, CarSendOrderVo order, SysUser currentUser) {
		CarState state = CarState.valueOf(order.getStatus());
		List<StateMachineAccess<CarState, CarEvent>> withAllRegions = stateMachine.getStateMachineAccessor().withAllRegions();
        for (StateMachineAccess<CarState, CarEvent> a : withAllRegions) {
            a.resetStateMachine(new DefaultStateMachineContext<CarState, CarEvent>(state, null, null, null));
        }
        return state;
	}

	private void persist(StateMachine<CarState, CarEvent> stateMachine, CarSendOrderVo order, SysUser user, CarState preState, CarEvent event, FlowInfoVo flowInfoVo) {
		CarState state = stateMachine.getState().getId();
		order.setStatus(state.toString());
		carOrderMapper.updateById(order);

		if(flowInfoVo == null) {
			flowInfoVo = new FlowInfoVo();
		}
        flowInfoVo.setFlowInfoId(CommonUtil.getUUID());
        flowInfoVo.setWorkOrderId(order.getCarSendOrderId());
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
