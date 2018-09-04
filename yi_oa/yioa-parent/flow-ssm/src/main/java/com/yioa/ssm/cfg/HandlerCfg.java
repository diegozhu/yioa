package com.yioa.ssm.cfg;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xseed.ssmflow.cfg.service.FlowPersist;
import com.yioa.ssm.domain.FlowHandlerVo;
import com.yioa.ssm.mapper.FlowInfoMapper;
import com.yioa.ssm.service.HandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *  对某环节执行人的配置
 * Created by tao on 2017-05-30.
 */
@Configuration
public class HandlerCfg {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Value("${flow.stage.handler}")
    private String handerCfgStr;


    @Autowired
    private FlowInfoMapper flowInfoMapper;

    @Bean
    public HandlerService handlerService() {

        TypeReference<HashMap<String, FlowHandlerVo>> typeRef
                = new TypeReference<HashMap<String, FlowHandlerVo>>() {
        };

        Map<String, FlowHandlerVo> map = null;
        try {
            map = MAPPER.readValue(handerCfgStr, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("########## error init HandlerService", e);
        }
        return new HandlerService(map,flowInfoMapper);
    }

}
