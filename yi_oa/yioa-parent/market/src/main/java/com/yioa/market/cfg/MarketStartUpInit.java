package com.yioa.market.cfg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.yioa.market.domain.MarketOrderVo;
import com.yioa.market.mapper.MarketOrderMapper;
import com.yioa.ssm.domain.FlowHandlerVo;
import com.yioa.ssm.service.HandlerService;
import com.yioa.ssm.service.ICustomerHandler;
import com.yioa.ssm.util.FlowUtil;
import com.yioa.ssm.util.MarketFlows;

/**
 * Created by tao on 2017-06-05.
 */
@Component
public class MarketStartUpInit {

    @Autowired
    private HandlerService handlerService;

    @Autowired
    private MarketOrderMapper marketOrderMapper;

    @PostConstruct
    public void init(){
        
        handlerService.addCustomerHandler(MarketFlows.MARKET_FLOW_DONE.name(), new ICustomerHandler() {
            @Override
            public FlowHandlerVo customerHandler(String flowStage, String orderId) {

                Assert.notNull(flowStage,"flowStage should not be null");
                Assert.notNull(orderId,"orderId should not be null");

                MarketOrderVo vo = marketOrderMapper.selectById(orderId);

                FlowHandlerVo flowHandlerVo = new FlowHandlerVo(vo.getCreateBy(),vo.getCreateById(), FlowUtil.USER_HANDLER_TYPE);
                return flowHandlerVo;
            }
        });
    }




}
