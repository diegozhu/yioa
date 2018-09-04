package com.yioa.ssm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.yioa.ssm.domain.FlowHandlerContextVo;
import com.yioa.ssm.domain.FlowHandlerVo;
import com.yioa.ssm.domain.FlowInfoVo;
import com.yioa.ssm.mapper.FlowInfoMapper;
import com.yioa.ssm.util.FlowUtil;

/**
 * Created by tao on 2017-05-30.
 */

public class HandlerService {

    private Logger logger = LoggerFactory.getLogger(getClass());


    private Map<String, FlowHandlerVo> flowHandlerVoMap;

    private FlowInfoMapper flowInfoMapper;

    private Map<String, ICustomerHandler> customerHandlerMap = new HashMap<>();

    public HandlerService(Map<String, FlowHandlerVo> flowHandlerVoMap, FlowInfoMapper flowInfoMapper) {
        this.flowHandlerVoMap = flowHandlerVoMap;
        this.flowInfoMapper = flowInfoMapper;
    }

    public Map<String, FlowHandlerVo> getFlowHandlerVoMap() {
        return flowHandlerVoMap;
    }

    public void setFlowHandlerVoMap(Map<String, FlowHandlerVo> flowHandlerVoMap) {
        this.flowHandlerVoMap = flowHandlerVoMap;
    }

    public void addCustomerHandler(String key, ICustomerHandler customerHandler) {
        if (this.customerHandlerMap.containsKey(key)) {
            throw new RuntimeException("tache " + key + " has configed!!!");
        }
        this.customerHandlerMap.put(key, customerHandler);
    }


//    /**
//     * 获取每个环节的处理人
//     * 回单环节的处理人，搞个简单的el表达式来配置
//     * @param flowStage
//     * @return
//     */
//    public FlowHandlerVo getFlowHandler(String flowStage, String flowInfoId) {
//
//        if (flowHandlerVoMap.containsKey(flowStage)) {
//            return flowHandlerVoMap.get(flowStage);
//        }
//
//        //没有处理人，返回null
//        return null;
//    }

    /**
     * 获取每个环节的处理人
     * 回单环节的处理人，搞个简单的el表达式来配置
     * <p>
     * <h>当前的支持</h>
     * <li> spel 与前面某环节的处理人相同，而且是取最后一个 </li>
     * <li> fixed 最常见的的，固定处理人，org/role/staff </li>
     * <li> customer 强行插入的自定义,使用PostConstruct 进行spring 容器启动完成后的初始化，针对制定环节，自定义处理 </li>
     *
     * @param flowStage
     * @param workOrderId
     * @return
     */
    public FlowHandlerVo getFlowHandler(String flowStage, String workOrderId) {

        FlowHandlerVo flowHandlerVo = null;

        if (flowHandlerVoMap.containsKey(flowStage)) {
            flowHandlerVo = flowHandlerVoMap.get(flowStage);

        }
        if (flowHandlerVo != null && StringUtils.isNotEmpty(flowHandlerVo.getUseSpel())) {

            if (flowHandlerVo.getUseSpel().equalsIgnoreCase("spel")) {
                //当前只支持，与前面某环节的处理人相同，而且是取最后一个
                return this.getFlowHandlerVoBySpel(flowStage, workOrderId, flowHandlerVo.getSpelStr());
            } else if (flowHandlerVo.getUseSpel().equalsIgnoreCase("fixed")) {
                return flowHandlerVo;
            } else if (flowHandlerVo.getUseSpel().equalsIgnoreCase("customer")) {
                //自己定义的扩展，都快记不得了，支持自定义某环节的处理人
                ICustomerHandler handler = this.customerHandlerMap.get(flowStage);
                Assert.notNull(handler, "configuration for " + flowStage + " should not be null");
                if (handler == null) {
                    throw new RuntimeException("configuration is miss for: " + flowStage);
                }
                return handler.customerHandler(flowStage, workOrderId);
            } else {
                throw new RuntimeException("not support yet: " + flowHandlerVo.getUseSpel());
            }
        }
        //没有处理人,返回固定值
        if (flowHandlerVo != null) {
            return flowHandlerVo;
        } else {
            ICustomerHandler handler = this.customerHandlerMap.get(flowStage);
            Assert.notNull(handler, "configuration for " + flowStage + " should not be null");
            if (handler == null) {
                throw new RuntimeException("configuration is miss for: " + flowStage);
            }
            return handler.customerHandler(flowStage, workOrderId);
        }

    }


    private FlowHandlerVo getFlowHandlerVoBySpel(String flowStage, String workOrderId, String spelStr) {

        logger.info("#####getFlowHandlerVoBySpel: {},{},{}", flowStage, workOrderId, spelStr);
        FlowHandlerContextVo flowHandlerContextVo = new FlowHandlerContextVo(workOrderId);

        List<FlowInfoVo> list = flowInfoMapper.selectList(new EntityWrapper<FlowInfoVo>().eq("work_order_id", workOrderId).orderBy("create_date"));
        for (FlowInfoVo tmp : list) {
            flowHandlerContextVo.addFlowInfoVo(tmp.getFlowStage(), tmp);
        }

        //************************************************************这几个步骤是可以缓存的，不必每次都跑
        // 使用key spelStr 做map
        SpelParserConfiguration config = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE,
                this.getClass().getClassLoader());
        SpelExpressionParser parser = new SpelExpressionParser(config);

        spelStr = StringUtils.replaceAll(spelStr, "#OA_FLOW#", flowStage);

        Expression exp = parser.parseExpression(spelStr);
        //************************************************************一直到这里 end

        EvaluationContext context = new StandardEvaluationContext(flowHandlerContextVo);

        FlowInfoVo flowInfoVo = exp.getValue(context, FlowInfoVo.class);

        FlowHandlerVo reFlowHandlerVo = null;

        if (flowInfoVo != null) {
            reFlowHandlerVo = new FlowHandlerVo(flowInfoVo.getCompleteUserName(), flowInfoVo.getCompleteUserId(), FlowUtil.USER_HANDLER_TYPE);
        }

        return reFlowHandlerVo;
    }


    /**
     * 获得默认处理人
     *
     * @return
     */
    public FlowHandlerVo getDefaultFlowHandler() {
        FlowHandlerVo flowHandlerVo = new FlowHandlerVo();

        flowHandlerVo.setHandlerByType(FlowUtil.USER_HANDLER_TYPE);
        flowHandlerVo.setHandlerBy("thinkgem");
        flowHandlerVo.setHandlerById("1");
        return flowHandlerVo;
    }


}
