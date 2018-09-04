package com.yioa.ssm.cfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

/**
 * Created by tao on 2017-05-28.
 */
@WithStateMachine
public class EventConfig {

    @Autowired
    private StateMachine stateMachine;

    private Logger logger = LoggerFactory.getLogger(getClass());



    @OnTransition(target = "FlOW_START")
    public void create() {
        logger.info("工单创建完成，待提交");
    }

    @OnTransition(source = "FlOW_START", target = "FLOW_MILESTONE")
    public void submit() {
        logger.info("用户完成提交，准备进行阶段回单");
    }

    @OnTransition(source = "FLOW_AUDIT", target = "FLOW_COMPLETE")
    public void auditNo() {
        logger.info("审批被打回，继续回单");
    }


    @OnTransition(source = "FLOW_AUDIT", target = "FLOW_MILESTONE")
    public void auditPass() {
        logger.info("审批pass，准备进行阶段回单");
    }

    @OnTransition(source = "FLOW_MILESTONE", target = "FLOW_COMPLETE")
    public void milestone() {
        logger.info("行阶段回单结束,准备回单");
    }

    @OnTransition(source = "FLOW_COMPLETE", target = "FLOW_AUDIT")
    public void complete() {
        logger.info("回单结束，开始回单审核");
    }

    @OnTransition(source = "FLOW_AUDIT", target = "FLOW_DONE")
    public void completeAudit() {
        logger.info("@@@@@@@@@@@@@@@@@@@@@@ completeAudit ,{}",stateMachine.isComplete());
        logger.info("回单审核结束，流程结束");
    }


}
