package com.yioa.oa.cfg;

import java.util.EnumSet;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.yioa.oa.cfg.OaStateMachineConfig.OaEvent;
import com.yioa.oa.cfg.OaStateMachineConfig.OaState;


@Configuration
@EnableStateMachine(name = OaStateMachineConfig.NAME)
public class OaStateMachineConfig extends StateMachineConfigurerAdapter<OaState, OaEvent> {

	public enum OaEvent {
		SUBMIT,
		MILESTONE,
		COMPLETE,
		AUDIT_PASS,
		AUDIT_NO,
		CANCEL,
		COMMENT
	}
	
	public enum OaState {
		DRAFT,
		START,
		MILESTONE,   // 阶段回单
		COMPLETE,   // 回单
		AUDIT_PASS,       // 审批
		AUDIT_NO,
		CANCELLED
	}
	
	public static final String NAME = "oaStateMachine";
	
    @Override
    public void configure(StateMachineStateConfigurer<OaState, OaEvent> states) throws Exception {
    	states.withStates().initial(OaState.START).states(EnumSet.allOf(OaState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OaState, OaEvent> transitions) throws Exception {
        transitions.withExternal()
        
        .source(OaState.DRAFT).target(OaState.START).event(OaEvent.SUBMIT).and().withExternal()
        .source(OaState.DRAFT).target(OaState.CANCELLED).event(OaEvent.CANCEL).and().withExternal()
        .source(OaState.DRAFT).target(OaState.DRAFT).event(OaEvent.COMMENT).and().withExternal()
        
        .source(OaState.START).target(OaState.MILESTONE).event(OaEvent.MILESTONE).and().withExternal()
        .source(OaState.START).target(OaState.CANCELLED).event(OaEvent.CANCEL).and().withExternal()
        .source(OaState.START).target(OaState.START).event(OaEvent.COMMENT).and().withExternal()
        
        .source(OaState.MILESTONE).target(OaState.MILESTONE).event(OaEvent.MILESTONE).and().withExternal()
        .source(OaState.MILESTONE).target(OaState.COMPLETE).event(OaEvent.COMPLETE).and().withExternal()
        .source(OaState.MILESTONE).target(OaState.MILESTONE).event(OaEvent.COMMENT).and().withExternal()
        
        .source(OaState.COMPLETE).target(OaState.AUDIT_NO).event(OaEvent.AUDIT_NO).and().withExternal()
        .source(OaState.COMPLETE).target(OaState.AUDIT_PASS).event(OaEvent.AUDIT_PASS).and().withExternal()
        .source(OaState.COMPLETE).target(OaState.COMPLETE).event(OaEvent.COMMENT).and().withExternal()
        
        .source(OaState.AUDIT_NO).target(OaState.MILESTONE).event(OaEvent.MILESTONE).and().withExternal()
        .source(OaState.AUDIT_NO).target(OaState.COMPLETE).event(OaEvent.COMPLETE).and().withExternal()
        .source(OaState.AUDIT_NO).target(OaState.AUDIT_NO).event(OaEvent.COMMENT).and().withExternal()

        .source(OaState.AUDIT_PASS).target(OaState.AUDIT_PASS).event(OaEvent.COMMENT).and().withExternal()

        .source(OaState.CANCELLED).target(OaState.CANCELLED).event(OaEvent.COMMENT).and().withExternal()
;
    }
}


@Configuration
class OaEventConverter extends WebMvcConfigurationSupport {
   @Override
   public FormattingConversionService mvcConversionService() {
       FormattingConversionService f = super.mvcConversionService();
       f.addConverter(new OaEnumConverter());
       return f;
   }
}

class OaEnumConverter implements Converter<String, OaEvent> {
    @Override
    public OaEvent convert(String source) {
       try {
          return OaEvent.valueOf(source.toUpperCase().trim());
       } catch(Exception e) {
          return null;
       }
    }
}