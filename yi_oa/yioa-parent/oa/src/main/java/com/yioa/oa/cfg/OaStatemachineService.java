package com.yioa.oa.cfg;

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
import com.yioa.oa.cfg.OaStateMachineConfig.OaEvent;
import com.yioa.oa.cfg.OaStateMachineConfig.OaState;
import com.yioa.oa.domain.WorkOrderVo;
import com.yioa.oa.service.impl.WorkOrderService;
import com.yioa.ssm.domain.FlowInfoVo;
import com.yioa.ssm.mapper.FlowInfoMapper;
import com.yioa.sys.domain.SysUser;

@Component
@WithStateMachine(name= OaStateMachineConfig.NAME, id = OaStateMachineConfig.NAME)
public class OaStatemachineService {
	
	@Autowired
	private WorkOrderService workOrderService;
	
	@Autowired
	private FlowInfoMapper flowInfoMapper;

	@Autowired
	private StateMachine<OaState, OaEvent> stateMachine;
	
    private static final Logger logger = LoggerFactory.getLogger(OaStatemachineService.class);

	
	public boolean sendEvent(OaEvent event, WorkOrderVo order, SysUser currentUser, FlowInfoVo flowInfoVo) {
        synchronized (String.valueOf(order.getWorkOrderId()).intern()) {
            boolean success = false;
			System.out.println("id=" + order.getWorkOrderId() + " 状态机 orderStateMachine:" + stateMachine);
            try {
            	stateMachine.start();
                //尝试恢复状态机状态
                OaState preState = restore(stateMachine, order, currentUser);
                System.out.println("id=" + order.getWorkOrderId() + " 状态机 orderStateMachine id=" + stateMachine.getId());
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
	
	public List<OaEvent> getAviableActions(WorkOrderVo order) {
		OaState state = OaState.valueOf(order.getStatus());
		ArrayList<OaEvent> eventsForState = stateMachine.getTransitions().stream().filter(p -> p.getSource().getId().equals(state)).map(p -> p.getTrigger().getEvent()).collect(Collectors.toCollection(ArrayList::new));
		return eventsForState;
	} 
	
	public StateMachine<OaState, OaEvent> getStatemachine() {
		return stateMachine;
	}
	
	private OaState restore(StateMachine<OaState, OaEvent> stateMachine, WorkOrderVo order, SysUser currentUser) {
		OaState state = OaState.valueOf(order.getStatus());
		List<StateMachineAccess<OaState, OaEvent>> withAllRegions = stateMachine.getStateMachineAccessor().withAllRegions();
        for (StateMachineAccess<OaState, OaEvent> a : withAllRegions) {
            a.resetStateMachine(new DefaultStateMachineContext<OaState, OaEvent>(state, null, null, null));
        }
        return state;
	}

	private void persist(StateMachine<OaState, OaEvent> stateMachine, WorkOrderVo order, SysUser user, OaState preState, OaEvent event, FlowInfoVo flowInfoVo) {
		OaState state = stateMachine.getState().getId();
		order.setStatus(state.toString());
		workOrderService.updateById(order);

		if(flowInfoVo == null) {
			flowInfoVo = new FlowInfoVo();
		}
        flowInfoVo.setFlowInfoId(CommonUtil.getUUID());
        flowInfoVo.setWorkOrderId(order.getWorkOrderId());
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
