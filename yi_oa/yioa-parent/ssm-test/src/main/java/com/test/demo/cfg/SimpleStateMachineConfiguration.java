package com.test.demo.cfg;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by tao on 2017-06-04.
 */
@Configuration
@EnableStateMachine
public class SimpleStateMachineConfiguration
        extends StateMachineConfigurerAdapter<String, String> {

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states)
            throws Exception {

        states
                .withStates()
                .initial("SS")
                .end("SF")
                .states(
                        new HashSet<String>(Arrays.asList("S1", "S2")));
//                .states(
//                        new HashSet<String>());

        states.withStates().states(new HashSet<String>(Arrays.asList("A1", "A2")));
    }

    @Override
    public void configure(
            StateMachineTransitionConfigurer<String, String> transitions)
            throws Exception {

        transitions.withExternal()
                .source("SS").target("S1").event("E1")
                .and().withExternal()
                .source("S1").target("S2").event("E2")
                .and().withExternal()
                .source("S2").target("SF").event("end")

                .and().withExternal()
                .source("SS").target("A1").event("EE1")
                .and().withExternal()
                .source("A1").target("A2").event("EE2")
                .and().withExternal()
                .source("A2").target("SF").event("eend");


    }
}