package com.yioa.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.yioa.ssm.domain.FlowHandlerVo;
import com.yioa.ssm.util.FlowUtil;
import com.yioa.ssm.util.OaFlows;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.UUID;

/**
 * Created by tao on 2017-05-29.
 */
public class Tmain {

    private static final ObjectMapper MAPPER = new ObjectMapper();


    public static void xx() throws JsonProcessingException {


        FlowHandlerVo audit = new FlowHandlerVo("thinkgem", "1", FlowUtil.USER_HANDLER_TYPE);

        FlowHandlerVo flowMilestone = new FlowHandlerVo("thinkgem", "1", FlowUtil.USER_HANDLER_TYPE);

        FlowHandlerVo flowComplete = new FlowHandlerVo("thinkgem", "1", FlowUtil.USER_HANDLER_TYPE);

        Map<String, FlowHandlerVo> map = ImmutableMap.of(OaFlows.FLOW_AUDIT.name(), audit, OaFlows.FLOW_MILESTONE.name(), flowMilestone, OaFlows.FLOW_COMPLETE.name(), flowComplete);

        System.out.println(MAPPER.writeValueAsString(map));
    }

    ;


    public static void main(String[] args) throws JsonProcessingException {
        UUID uuid = UUID.randomUUID();
        System.out.println(StringUtils.replaceAll(String.valueOf(uuid), "-", ""));

        xx();
    }
}
