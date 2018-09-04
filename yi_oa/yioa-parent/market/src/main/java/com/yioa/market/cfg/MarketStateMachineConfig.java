package com.yioa.market.cfg;

import java.util.EnumSet;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.yioa.market.cfg.MarketStateMachineConfig.MarketEvent;
import com.yioa.market.cfg.MarketStateMachineConfig.MarketState;


@Configuration
@EnableStateMachine(name = MarketStateMachineConfig.NAME)
public class MarketStateMachineConfig extends StateMachineConfigurerAdapter<MarketState, MarketEvent> {

	public enum MarketEvent {
		SUBMIT,
		COMPLETE,
		AUDIT_PASS,
		AUDIT_NO,
		CANCEL,
		COMMENT,
		SIGN //签到
	}
	
	public enum MarketState {
		DRAFT,
		START,
		COMPLETE,
		AUDIT_PASS,
		AUDIT_NO,
		CANCELLED
	}
	
	public static final String NAME = "marketStateMachine";
	
    @Override
    public void configure(StateMachineStateConfigurer<MarketState, MarketEvent> states) throws Exception {
    	states.withStates().initial(MarketState.START).states(EnumSet.allOf(MarketState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<MarketState, MarketEvent> transitions) throws Exception {
        transitions.withExternal()
        
      //comment
        .event(MarketEvent.COMMENT).source(MarketState.DRAFT).target(MarketState.DRAFT).and().withExternal()
        .event(MarketEvent.COMMENT).source(MarketState.START).target(MarketState.START).and().withExternal()
        .event(MarketEvent.COMMENT).source(MarketState.AUDIT_NO).target(MarketState.AUDIT_NO).and().withExternal()
        .event(MarketEvent.COMMENT).source(MarketState.COMPLETE).target(MarketState.COMPLETE).and().withExternal()
        .event(MarketEvent.COMMENT).source(MarketState.AUDIT_PASS).target(MarketState.AUDIT_PASS).and().withExternal()
        .event(MarketEvent.COMMENT).source(MarketState.CANCELLED).target(MarketState.CANCELLED).and().withExternal()
        
        //签到
        .event(MarketEvent.SIGN).source(MarketState.DRAFT).target(MarketState.DRAFT).and().withExternal()
        .event(MarketEvent.SIGN).source(MarketState.START).target(MarketState.START).and().withExternal()
        .event(MarketEvent.SIGN).source(MarketState.AUDIT_NO).target(MarketState.AUDIT_NO).and().withExternal()
        .event(MarketEvent.SIGN).source(MarketState.COMPLETE).target(MarketState.COMPLETE).and().withExternal()
        .event(MarketEvent.SIGN).source(MarketState.AUDIT_PASS).target(MarketState.AUDIT_PASS).and().withExternal()
        .event(MarketEvent.SIGN).source(MarketState.CANCELLED).target(MarketState.CANCELLED).and().withExternal()
        
        //transitions
        .source(MarketState.DRAFT).event(MarketEvent.SUBMIT).target(MarketState.START).and().withExternal()
        .source(MarketState.DRAFT).event(MarketEvent.CANCEL).target(MarketState.CANCELLED).and().withExternal()
        
        .source(MarketState.START).event(MarketEvent.COMPLETE).target(MarketState.COMPLETE).and().withExternal()
        .source(MarketState.START).event(MarketEvent.CANCEL).target(MarketState.CANCELLED).and().withExternal()
        
        .source(MarketState.COMPLETE).event(MarketEvent.AUDIT_NO).target(MarketState.AUDIT_NO).and().withExternal()
        .source(MarketState.COMPLETE).event(MarketEvent.AUDIT_PASS).target(MarketState.AUDIT_PASS).and().withExternal()
        
        .source(MarketState.AUDIT_NO).event(MarketEvent.COMPLETE).target(MarketState.COMPLETE).and().withExternal()
;
    }
}


@Configuration
class MarketEventConverter extends WebMvcConfigurationSupport {
   @Override
   public FormattingConversionService mvcConversionService() {
       FormattingConversionService f = super.mvcConversionService();
       f.addConverter(new MarketEnumConverter());
       return f;
   }
}

class MarketEnumConverter implements Converter<String, MarketEvent> {
    @Override
    public MarketEvent convert(String source) {
       try {
          return MarketEvent.valueOf(source.toUpperCase().trim());
       } catch(Exception e) {
          return null;
       }
    }
}