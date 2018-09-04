package com.test.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;

/**
 * Created by tao on 2017-06-04.
 */
@SpringBootApplication
public class DemoApp implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StateMachine stateMachine;

    public static void main(String[] args) {
        SpringApplication.run(DemoApp.class, args);
    }



    @Override
    public void run(String... strings) throws Exception {
        State s = null;
        stateMachine.start();
        stateMachine.sendEvent("E1");
        s = stateMachine.getState();
        logger.info("################ state is {}",s.getId());
        stateMachine.sendEvent("E2");
        s = stateMachine.getState();
        logger.info("################ state is {}",s.getId());
        logger.info("################ is end: {}",stateMachine.isComplete());
        stateMachine.sendEvent("end");
        s = stateMachine.getState();
        logger.info("################ state is {}",s.getId());
        logger.info("################ is end: {}",stateMachine.isComplete());
        stateMachine.stop();

        stateMachine.start();
        stateMachine.sendEvent("EE1");
        s = stateMachine.getState();
        logger.info("################ state is {}",s.getId());
        stateMachine.sendEvent("EE2");
        s = stateMachine.getState();
        logger.info("################ state is {}",s.getId());
        logger.info("################ is end: {}",stateMachine.isComplete());
        stateMachine.sendEvent("eend");
        s = stateMachine.getState();
        logger.info("################ state is {}",s.getId());
        logger.info("################ is end: {}",stateMachine.isComplete());
        stateMachine.stop();




    }
}
