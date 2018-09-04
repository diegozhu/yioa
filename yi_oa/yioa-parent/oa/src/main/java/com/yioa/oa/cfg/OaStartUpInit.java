package com.yioa.oa.cfg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.yioa.oa.domain.WorkOrderVo;
import com.yioa.oa.mapper.WorkOrderMapper;
import com.yioa.ssm.domain.FlowHandlerVo;
import com.yioa.ssm.service.HandlerService;
import com.yioa.ssm.service.ICustomerHandler;
import com.yioa.ssm.util.FlowUtil;
import com.yioa.ssm.util.OaFlows;

/**
 * Created by tao on 2017-06-05.
 */
@Component
public class OaStartUpInit {

    @Autowired
    private HandlerService handlerService;

    @Autowired
    private WorkOrderMapper workOrderMapper;

    @PostConstruct
    public void init() {
    	handlerService.addCustomerHandler(OaFlows.FLOW_AUDIT.name(), new ICustomerHandler() {
            @Override
            public FlowHandlerVo customerHandler(String flowStage, String orderId) {

                Assert.notNull(flowStage, "flowStage should not be null");
                Assert.notNull(orderId, "orderId should not be null");

                WorkOrderVo vo = workOrderMapper.selectById(orderId);

                FlowHandlerVo flowHandlerVo = new FlowHandlerVo(vo.getCreateBy(), vo.getCreateById(), FlowUtil.USER_HANDLER_TYPE);

                return flowHandlerVo;
            }
        });
    	
    	handlerService.addCustomerHandler(OaFlows.FLOW_DONE.name(), new ICustomerHandler() {
            @Override
            public FlowHandlerVo customerHandler(String flowStage, String orderId) {

                Assert.notNull(flowStage, "flowStage should not be null");
                Assert.notNull(orderId, "orderId should not be null");

                WorkOrderVo vo = workOrderMapper.selectById(orderId);

                FlowHandlerVo flowHandlerVo = new FlowHandlerVo(vo.getCreateBy(), vo.getCreateById(), FlowUtil.USER_HANDLER_TYPE);

                return flowHandlerVo;
            }
        });
    }


}
