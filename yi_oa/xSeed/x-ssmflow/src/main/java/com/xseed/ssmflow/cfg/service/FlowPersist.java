/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xseed.ssmflow.cfg.service;

import com.xseed.ssmflow.cfg.util.SsmFlowCnst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler.PersistStateChangeListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;


public class FlowPersist {


    private Logger logger = LoggerFactory.getLogger(FlowPersist.class);

    private final PersistStateMachineHandler handler;

    private IPersistCallBackService persistCallBackService;


    private final PersistStateChangeListener listener = new LocalPersistStateChangeListener();

    public FlowPersist(PersistStateMachineHandler handler, IPersistCallBackService persistCallBackService) {
        this.handler = handler;
        this.persistCallBackService = persistCallBackService;
        this.handler.addPersistStateChangeListener(listener);
    }


    public boolean change(Object param, String event, String currentState) {
        boolean bool = handler.handleEventWithState(MessageBuilder.withPayload(event).setHeader(SsmFlowCnst.FLOW_CUST_PARAM, param).build(), currentState);
        //这里也可以加一个callback，但是暂时看没有必要
        if (bool) {

            logger.info("######### 流转成功");
        }

        return bool;
    }


    private class LocalPersistStateChangeListener implements PersistStateChangeListener {

        @Override
        public void onPersist(State<String, String> state, Message<String> message,
                              Transition<String, String> transition, StateMachine<String, String> stateMachine) {
            persistCallBackService.callBack(state, message, transition, stateMachine);
        }
    }


}
