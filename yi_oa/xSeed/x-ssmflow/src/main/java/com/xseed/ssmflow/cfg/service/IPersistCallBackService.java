package com.xseed.ssmflow.cfg.service;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

/**
 * Created by tao on 2017-05-30.
 */
public interface IPersistCallBackService {

    public void callBack(State<String, String> state, Message<String> message,
                         Transition<String, String> transition, StateMachine<String, String> stateMachine);


}
