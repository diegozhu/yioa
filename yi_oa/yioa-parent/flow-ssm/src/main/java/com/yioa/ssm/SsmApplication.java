package com.yioa.ssm;

import com.yioa.ssm.service.HandlerService;
import com.yioa.ssm.util.OaEvents;
import com.yioa.ssm.util.OaFlows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;

/**
 * Created by tao on 2017-05-28.
 */
@SpringBootApplication
public class SsmApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(SsmApplication.class);

    @Autowired
    private StateMachine stateMachine;

//    @Autowired
//    private Persist persist;



    public static void main(String[] args) {
        SpringApplication.run(SsmApplication.class, args);
    }


    private void basicTest() {
        stateMachine.start();

        stateMachine.sendEvent(OaEvents.SUBMIT.name());
        State<OaFlows, OaEvents> state = stateMachine.getState();
        logger.info("######### state: {}", state);
        stateMachine.sendEvent(OaEvents.AUDIT_NO.name());
        state = stateMachine.getState();
        logger.info("######### state: {}", state);
        stateMachine.sendEvent(OaEvents.COMPLETE.name());

       state = stateMachine.getState();
        logger.info("######### state: {}", state);
    }

    private void eventTest() {
//        String wokorderId = "fff5cb67cd384c889f2fd4496c876906";
//        persist.change(wokorderId, OaEvents.CAR_AUDIT_PASS);
//        persist.change(wokorderId, OaEvents.MILESTONE);
//        persist.change(wokorderId, OaEvents.CAR_COMPLETE);
//
//        String wokorderId2 = "d09d1c1fe89946dcb6798b06468815cc";
//        persist.change(wokorderId2, OaEvents.CAR_SUBMIT);
//        persist.change(wokorderId2, OaEvents.CAR_AUDIT_NO);
//
//        String wokorderId3 = "9e8cab7843954597a3bec8e784c0c60f";
//        persist.change(wokorderId3, OaEvents.CAR_COMPLETE);

    }


    @Override
    public void run(String... args) throws Exception {
//        basicTest();
//        eventTest();

    }
}
