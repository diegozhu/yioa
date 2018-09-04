package com.yioa.test;

import com.yioa.ssm.domain.FlowHandlerContextVo;
import com.yioa.ssm.domain.FlowInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Created by tao on 2017-06-03.
 */
public class SpelMain {

    public static void main(String[] args) {

        FlowHandlerContextVo flowHandlerContextVo = new FlowHandlerContextVo("123");
        FlowInfoVo f1 = new FlowInfoVo();
        f1.setFlowStage("FLOW_XX");
        f1.setFlowInfoId("xxxx1");


        FlowInfoVo f2 = new FlowInfoVo();
        f2.setFlowStage("FLOW_MILESTONE");
        f2.setFlowInfoId("xxxx2");

        flowHandlerContextVo.addFlowInfoVo("FLOW_XX",f1);
        flowHandlerContextVo.addFlowInfoVo("FLOW_MILESTONE",f2);


        SpelParserConfiguration config = new SpelParserConfiguration();
        SpelExpressionParser parser = new SpelExpressionParser(config);

       String spelStr = "flowInfoVoMap['FLOW_MILESTONE']";

        Expression exp = parser.parseExpression(spelStr);
        //************************************************************一直到这里 end

        EvaluationContext context = new StandardEvaluationContext(flowHandlerContextVo);

        FlowInfoVo flowInfoVo = exp.getValue(context, FlowInfoVo.class);
        System.out.print(flowInfoVo);
    }
}
