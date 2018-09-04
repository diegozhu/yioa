package com.yioa.test;

/**
 * Created by tao on 2017-05-25.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yioa.common.util.CommonUtil;
import com.yioa.core.CoreApplication;
import com.yioa.oa.domain.WorkOrderVo;
import com.yioa.oa.mapper.WorkOrderMapper;
import com.yioa.oa.service.IWorkOrderService;
import com.yioa.ssm.domain.FlowInfoVo;
import com.yioa.ssm.mapper.FlowInfoMapper;
import com.yioa.ssm.util.OaEvents;
import com.yioa.ssm.util.OaFlows;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

//@EnableWebMvc
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
@SpringApplicationConfiguration(classes = CoreApplication.class)
public class OaFlowApplicationTest {


    private static final String rootUrl = "http://127.0.0.1:8080";

//    @Autowired
//    private MockMvc mockMvc;

//    @Autowired
//    private TestRestTemplate restTemplate;

    @Autowired
    private IWorkOrderService workOrderService;

    @Autowired
    private FlowInfoMapper flowInfoMapper;

    @Autowired
    private WorkOrderMapper workOrderMapper;


    private static final Logger LOGGER = LoggerFactory.getLogger(OaFlowApplicationTest.class);

    private static ObjectMapper mapper = new ObjectMapper();

    private static String testWorkOrderId = CommonUtil.getUUID();


    @Test
//    @Rollback
    public void flowtestAll() throws Exception {

        WorkOrderVo workOrderVo = new WorkOrderVo();

        //FIXME 测试的时候才需要这么干
        workOrderVo.setWorkOrderId(testWorkOrderId);

        workOrderVo.setOrganiser("thinkgem");
        workOrderVo.setCoOrganiser("sd_admin");
        workOrderVo.setMilestoneDate(new Date());
        workOrderVo.setReqCompleteDate(new Date());
        workOrderVo.setWorkOrderContent("吃饭了吗");
        workOrderVo.setWorkOrderSubject("chifan");
        workOrderVo.setWorkOrderType("请示");



        String reStr = workOrderService.workorderSubmit("1", "thinkgem", workOrderVo);
        Assert.assertEquals("1",reStr);

        WorkOrderVo ww1 = this.workOrderMapper.selectById(testWorkOrderId);
        FlowInfoVo ff = this.flowInfoMapper.selectById(ww1.getFlowInfoId());
        Assert.assertEquals(OaFlows.FLOW_MILESTONE.name(),ff.getFlowStage());
        Assert.assertEquals(OaFlows.FLOW_MILESTONE.name(),ww1.getFlowStage());




        WorkOrderVo tVo = this.workOrderMapper.selectById(testWorkOrderId);
        FlowInfoVo flowInfoVo = new FlowInfoVo();
        flowInfoVo.setFlowInfoId(tVo.getFlowInfoId());

        flowInfoVo.setNotes("让你阶段回单");
        flowInfoVo.setAttachFile("xxxxxxxxxxxx阶段回单");
        String str = workOrderService.flowSubmit("1", "thinkgem",testWorkOrderId, OaEvents.MILESTONE.name(), null,flowInfoVo);
        Assert.assertEquals("1",str);
        WorkOrderVo w1 = this.workOrderMapper.selectById(testWorkOrderId);
        FlowInfoVo f1 = this.flowInfoMapper.selectById(w1.getFlowInfoId());
        Assert.assertEquals(OaFlows.FLOW_COMPLETE.name(),f1.getFlowStage());
        Assert.assertEquals(OaFlows.FLOW_COMPLETE.name(),w1.getFlowStage());



        flowInfoVo.setFlowInfoId(f1.getFlowInfoId());
        flowInfoVo.setNotes("让你回单");
        flowInfoVo.setAttachFile("xxxxxxxxxxxx回单");
        str = workOrderService.flowSubmit("1", "thinkgem",testWorkOrderId, OaEvents.COMPLETE.name(), null,flowInfoVo);
        Assert.assertEquals("1",str);
        WorkOrderVo w2 = this.workOrderMapper.selectById(testWorkOrderId);
        FlowInfoVo f2 = this.flowInfoMapper.selectById(w2.getFlowInfoId());
        Assert.assertEquals(OaFlows.FLOW_AUDIT.name(),f2.getFlowStage());
        Assert.assertEquals(OaFlows.FLOW_AUDIT.name(),w2.getFlowStage());


        flowInfoVo.setFlowInfoId(f2.getFlowInfoId());
        flowInfoVo.setNotes("同意你归档");
        flowInfoVo.setAttachFile("xxxxxxxxxxxx同意你归档");
        str = workOrderService.flowSubmit("1", "thinkgem",testWorkOrderId, OaEvents.AUDIT_PASS.name(), null,flowInfoVo);
        Assert.assertEquals("1",str);
        WorkOrderVo w3 = this.workOrderMapper.selectById(testWorkOrderId);
        Assert.assertEquals(OaFlows.FLOW_DONE.name(),w3.getFlowStage());



    }





}
