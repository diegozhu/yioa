package com.yioa.test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.testng.annotations.Test;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler.PersistStateChangeListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

/**
 * Created by tao on 2017-05-29.
 */
public class PersistStateMachineHandlerTests {


    @Test
    public void testAcceptedStateChangeViaPersist() throws Exception {
        StateMachine<String,String> stateMachine = buildTestStateMachine();

        PersistStateMachineHandler handler = new PersistStateMachineHandler(stateMachine);
        handler.afterPropertiesSet();
        handler.start();

        TestPersistStateChangeListener listener = new TestPersistStateChangeListener();
        handler.addPersistStateChangeListener(listener);

        Message<String> event = MessageBuilder.withPayload("E2").build();
        boolean accepted = handler.handleEventWithState(event, "S1");
        assertThat(accepted, is(true));
        assertThat(listener.latch.await(1, TimeUnit.SECONDS), is(true));
        assertThat(stateMachine.getState().getIds(), containsInAnyOrder("S2"));
    }

    @Test
    public void testNotAcceptedStateChangeViaPersist() throws Exception {
        StateMachine<String,String> stateMachine = buildTestStateMachine();

        PersistStateMachineHandler handler = new PersistStateMachineHandler(stateMachine);
        handler.afterPropertiesSet();
        handler.start();

        TestPersistStateChangeListener listener = new TestPersistStateChangeListener();
        handler.addPersistStateChangeListener(listener);

        Message<String> event = MessageBuilder.withPayload("E1").build();
        boolean accepted = handler.handleEventWithState(event, "S1");
        assertThat(accepted, is(false));
        assertThat(listener.latch.await(1, TimeUnit.SECONDS), is(false));
        assertThat(stateMachine.getState().getIds(), containsInAnyOrder("S1"));
    }

    private class TestPersistStateChangeListener implements PersistStateChangeListener {

        CountDownLatch latch = new CountDownLatch(1);

        @Override
        public void onPersist(State<String, String> state, Message<String> message,
                              Transition<String, String> transition, StateMachine<String, String> stateMachine) {
            latch.countDown();
        }

    }

    private static StateMachine<String, String> buildTestStateMachine()
            throws Exception {
        StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
                .withConfiguration()
                .taskExecutor(new SyncTaskExecutor())
                .autoStartup(true);

        builder.configureStates()
                .withStates()
                .initial("SI")
                .state("S1")
                .state("S2");

        builder.configureTransitions()
                .withExternal()
                .source("SI").target("S1").event("E1")
                .and()
                .withExternal()
                .source("S1").target("S2").event("E2");
        builder.configureConfiguration().withConfiguration().beanFactory(new StaticListableBeanFactory());
        return builder.build();
    }

}
