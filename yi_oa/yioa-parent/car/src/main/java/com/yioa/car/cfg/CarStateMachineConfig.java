package com.yioa.car.cfg;

import java.util.EnumSet;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.yioa.car.cfg.CarStateMachineConfig.CarEvent;
import com.yioa.car.cfg.CarStateMachineConfig.CarState;


@Configuration
@EnableStateMachine(name = CarStateMachineConfig.NAME)
public class CarStateMachineConfig extends StateMachineConfigurerAdapter<CarState, CarEvent> {

	public enum CarEvent {
		SUBMIT,
		COMPLETE,
		PRE_AUDIT_PASS,
		PRE_AUDIT_NO,
		CANCEL,
		COMMENT
	}
	
	public enum CarState {
		DRAFT,
		START,
		PRE_AUDIT_PASS,       // 审批
		PRE_AUDIT_NO,
		COMPLETE,   // 回单
		CANCELLED
	}
	
	public static final String NAME = "carStateMachine";
	
    @Override
    public void configure(StateMachineStateConfigurer<CarState, CarEvent> states) throws Exception {
    	states.withStates().initial(CarState.START).states(EnumSet.allOf(CarState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<CarState, CarEvent> transitions) throws Exception {
        transitions.withExternal()
        
        //comments
        .event(CarEvent.COMMENT).source(CarState.DRAFT).target(CarState.DRAFT).and().withExternal()
        .event(CarEvent.COMMENT).source(CarState.PRE_AUDIT_NO).target(CarState.PRE_AUDIT_NO).and().withExternal()
        .event(CarEvent.COMMENT).source(CarState.START).target(CarState.START).and().withExternal()
        .event(CarEvent.COMMENT) .source(CarState.PRE_AUDIT_PASS).target(CarState.PRE_AUDIT_PASS).and().withExternal()
        .event(CarEvent.COMMENT).source(CarState.COMPLETE).target(CarState.COMPLETE).and().withExternal()
        .event(CarEvent.COMMENT).source(CarState.CANCELLED).target(CarState.CANCELLED).and().withExternal()
        
        //transitions
        .source(CarState.DRAFT).event(CarEvent.SUBMIT).target(CarState.START).and().withExternal()
        .source(CarState.DRAFT).event(CarEvent.CANCEL).target(CarState.CANCELLED).and().withExternal()
        
        .source(CarState.START).event(CarEvent.PRE_AUDIT_PASS).target(CarState.PRE_AUDIT_PASS).and().withExternal()
        .source(CarState.START).event(CarEvent.PRE_AUDIT_NO).target(CarState.PRE_AUDIT_NO).and().withExternal()

        .source(CarState.PRE_AUDIT_PASS).target(CarState.COMPLETE).event(CarEvent.COMPLETE).and().withExternal()
        ;
    }
}


@Configuration
class CarEventConverter extends WebMvcConfigurationSupport {
   @Override
   public FormattingConversionService mvcConversionService() {
       FormattingConversionService f = super.mvcConversionService();
       f.addConverter(new CarEnumConverter());
       return f;
   }
}

class CarEnumConverter implements Converter<String, CarEvent> {
    @Override
    public CarEvent convert(String source) {
       try {
          return CarEvent.valueOf(source.toUpperCase().trim());
       } catch(Exception e) {
          return null;
       }
    }
}