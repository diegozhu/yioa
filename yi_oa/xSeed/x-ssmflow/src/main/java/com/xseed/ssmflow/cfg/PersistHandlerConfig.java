package com.xseed.ssmflow.cfg;

import com.xseed.ssmflow.cfg.service.IPersistCallBackService;
import com.xseed.ssmflow.cfg.service.FlowPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;

/**
 * Created by tao on 2017-05-30.
 */
@Configuration
public class PersistHandlerConfig {

    @Autowired
    private StateMachine stateMachine;


    @Autowired
    private IPersistCallBackService persistCallBackService;

    @Bean
    public FlowPersist persist() {
        return new FlowPersist(persistStateMachineHandler(),persistCallBackService);
    }

    @Bean
    public PersistStateMachineHandler persistStateMachineHandler() {
        return new PersistStateMachineHandler(stateMachine);
    }
}