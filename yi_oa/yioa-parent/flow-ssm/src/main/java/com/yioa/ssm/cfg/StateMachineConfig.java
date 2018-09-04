package com.yioa.ssm.cfg;

import java.util.Arrays;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import com.yioa.ssm.util.CarEvents;
import com.yioa.ssm.util.CarFlows;
import com.yioa.ssm.util.MarketEvents;
import com.yioa.ssm.util.MarketFlows;
import com.yioa.ssm.util.OaEvents;
import com.yioa.ssm.util.OaFlows;
import com.yioa.ssm.util.PanelEvents;
import com.yioa.ssm.util.PanelFlows;

/**
 * Created by tao on 2017-05-28.
 */
@Configuration
@EnableStateMachine
public class StateMachineConfig extends StateMachineConfigurerAdapter<String, String> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states)
            throws Exception {
    	logger.debug("");
        //add oa_flow
        states.withStates().initial(OaFlows.FlOW_START.name()).end(OaFlows.FLOW_DONE.name())
                .state(OaFlows.FLOW_MILESTONE.name())
                .state(OaFlows.FLOW_COMPLETE.name())
                .state(OaFlows.FLOW_AUDIT.name());

        //add car_flow
        states.withStates().states(new HashSet<String>(Arrays.asList(CarFlows.CAR_FLOW_AUDIT.name(),CarFlows.CAR_FLOW_DRIVER_COMPLETE.name(), CarFlows.CAR_FLOW_COMPLETE.name())));
        states.withStates().states(new HashSet<String>(Arrays.asList(PanelFlows.PANEL_FLOW_AUDIT.name(),PanelFlows.PANEL_FLOW_COMPLETE.name())));
        states.withStates().states(new HashSet<String>(Arrays.asList(MarketFlows.MARKET_FlOW_START.name(),MarketFlows.MARKET_FLOW_DONE.name(),
                MarketFlows.MARKET_FLOW_MILESTONE.name(),MarketFlows.MARKET_FLOW_COMPLETE.name(),MarketFlows.MARKET_FLOW_AUDIT.name())));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions)
            throws Exception {
        transitions
                //启动->待阶段回单
                .withExternal()
                .source(OaFlows.FlOW_START.name()).target(OaFlows.FLOW_MILESTONE.name()).event(OaEvents.SUBMIT.name())

                //待阶段回单->待回单
                .and()
                .withExternal()
                .source(OaFlows.FLOW_MILESTONE.name()).target(OaFlows.FLOW_COMPLETE.name()).event(OaEvents.MILESTONE.name())

                //待阶段回单->待审批归档
                .and()
                .withExternal()
                .source(OaFlows.FLOW_MILESTONE.name()).target(OaFlows.FLOW_AUDIT.name()).event(OaEvents.COMPLETE.name())

                //待回单->待审批归档
                .and()
                .withExternal()
                .source(OaFlows.FLOW_COMPLETE.name()).target(OaFlows.FLOW_AUDIT.name()).event(OaEvents.COMPLETE.name())

                //待审批归档->待回单
                .and()
                .withExternal()
                .source(OaFlows.FLOW_AUDIT.name()).target(OaFlows.FLOW_COMPLETE.name()).event(OaEvents.AUDIT_NO.name())

                //待审批归档->待归档
                .and()
                .withExternal()
                .source(OaFlows.FLOW_AUDIT.name()).target(OaFlows.FLOW_DONE.name()).event(OaEvents.AUDIT_PASS.name())


                //***************************  car流程
                .and()
                .withExternal()
                .source(OaFlows.FlOW_START.name()).target(CarFlows.CAR_FLOW_AUDIT.name()).event(CarEvents.CAR_SUBMIT.name())

                .and()
                .withExternal()
                .source(CarFlows.CAR_FLOW_AUDIT.name()).target(CarFlows.CAR_FLOW_DRIVER_COMPLETE.name()).event(CarEvents.CAR_AUDIT_PASS.name())
                
                .and()
                .withExternal()
                .source(CarFlows.CAR_FLOW_DRIVER_COMPLETE.name()).target(CarFlows.CAR_FLOW_COMPLETE.name()).event(CarEvents.CAR_DRIVER_COMPLETE.name())

                .and()
                .withExternal()
                .source(CarFlows.CAR_FLOW_AUDIT.name()).target(OaFlows.FLOW_DONE.name()).event(CarEvents.CAR_AUDIT_NO.name())

                .and()
                .withExternal()
                .source(CarFlows.CAR_FLOW_COMPLETE.name()).target(OaFlows.FLOW_DONE.name()).event(CarEvents.CAR_COMPLETE.name())
                //***************************  任务版流程
                .and()
                .withExternal()
                .source(OaFlows.FlOW_START.name()).target(PanelFlows.PANEL_FLOW_AUDIT.name()).event(PanelEvents.PANEL_SUBMIT.name())

                .and()
                .withExternal()
                .source(PanelFlows.PANEL_FLOW_AUDIT.name()).target(PanelFlows.PANEL_FLOW_COMPLETE.name()).event(PanelEvents.PANEL_AUDIT_PASS.name())

                .and()
                .withExternal()
                .source(PanelFlows.PANEL_FLOW_AUDIT.name()).target(PanelFlows.FLOW_DONE.name()).event(PanelEvents.PANEL_AUDIT_NO.name())

                .and()
                .withExternal()
                .source(PanelFlows.PANEL_FLOW_COMPLETE.name()).target(PanelFlows.FLOW_DONE.name()).event(PanelEvents.PANEL_COMPLETE.name())

                //***************************  营销单流程
               .and()
               .withExternal()
               .source(MarketFlows.MARKET_FlOW_START.name()).target(MarketFlows.MARKET_FLOW_MILESTONE.name()).event(MarketEvents.MARKET_SUBMIT.name())

                //待阶段回单->待回单
                .and()
                .withExternal()
                .source(MarketFlows.MARKET_FLOW_MILESTONE.name()).target(MarketFlows.MARKET_FLOW_COMPLETE.name()).event(MarketEvents.MARKET_MILESTONE.name())

                //待阶段回单->待审批归档
                .and()
                .withExternal()
                .source(MarketFlows.MARKET_FLOW_MILESTONE.name()).target(MarketFlows.MARKET_FLOW_AUDIT.name()).event(MarketEvents.MARKET_COMPLETE.name())

                //待回单->待审批归档
                .and()
                .withExternal()
                .source(MarketFlows.MARKET_FLOW_COMPLETE.name()).target(MarketFlows.MARKET_FLOW_AUDIT.name()).event(MarketEvents.MARKET_COMPLETE.name())

                //待审批归档->待回单
                .and()
                .withExternal()
                .source(MarketFlows.MARKET_FLOW_AUDIT.name()).target(MarketFlows.MARKET_FLOW_COMPLETE.name()).event(MarketEvents.MARKET_AUDIT_NO.name())
                
                //待审批归档->待归档
                .and()
                .withExternal()
                .source(MarketFlows.MARKET_FLOW_AUDIT.name()).target(MarketFlows.MARKET_FLOW_DONE.name()).event(MarketEvents.MARKET_AUDIT_PASS.name());
    }


    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config)
            throws Exception {
        config.withConfiguration().machineId("oaStateMachine");
    }
//
//    @Bean
//    public StateMachineListener<OaOaStates, OaOaEvents> listener() {
//        return new StateMachineListenerAdapter<OaOaStates, OaOaEvents>() {
//            @Override
//            public void transition(Transition<OaOaStates, OaOaEvents> transition) {
//                if(transition.getTarget().getId() == OaOaStates.OaStates_STRAT) {
//                    logger.info("草稿已存创建，待提交");
//                    return;
//                }
//                if(transition.getSource().getId() == OaOaStates.OaStates_STRAT
//                        && transition.getTarget().getId() == OaOaStates.OaStates_AUDIT) {
//                    logger.info("用户完成提交，待审批");
//                    return;
//                }
//                if(transition.getSource().getId() == OaOaStates.OaStates_AUDIT
//                        && transition.getTarget().getId() == OaOaStates.OaStates_DONE) {
//                    logger.info("审批被打回，结束");
//                    return;
//                }
//
//                if(transition.getSource().getId() == OaOaStates.OaStates_AUDIT
//                        && transition.getTarget().getId() == OaOaStates.OaStates_MILESTONE) {
//                    logger.info("审批pass，准备进行阶段回单");
//                    return;
//                }
//
//                if(transition.getSource().getId() == OaOaStates.OaStates_MILESTONE
//                        && transition.getTarget().getId() == OaOaStates.OaStates_COMPLETE) {
//                    logger.info("行阶段回单结束,准备回单");
//                    return;
//                }
//
//                if(transition.getSource().getId() == OaOaStates.OaStates_COMPLETE
//                        && transition.getTarget().getId() == OaOaStates.OaStates_DONE) {
//                    logger.info("回单结束，流程结束");
//                    return;
//                }
//            }
//        };
//    }
}
