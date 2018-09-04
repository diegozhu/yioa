package com.yioa.test;

/**
 * Created by tao on 2017-05-25.
 */

import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yioa.common.util.CommonUtil;
import com.yioa.core.CoreApplication;
import com.yioa.oa.domain.WorkOrderVo;
import com.yioa.oa.mapper.WorkOrderMapper;
import com.yioa.oa.service.IWorkOrderService;
import com.yioa.ssm.util.OaFlows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

//@EnableWebMvc
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CoreApplication.class)
public class ApplicationTests {

    @Autowired
    private WorkOrderMapper workOrderMapper;

    @Autowired
    private IWorkOrderService workOrderService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationTests.class);

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
//    @Rollback
    public void findByName() throws Exception {

        WorkOrderVo workOrderVo = new WorkOrderVo();
        workOrderVo.setCoOrganiser("2");
        workOrderVo.setOrganiser("1");
        workOrderVo.setCreateBy("admin");
        workOrderVo.setCreateById("1");
        workOrderVo.setWorkOrderSubject("ererre");
        workOrderVo.setWorkOrderId(CommonUtil.getUUID());
        workOrderVo.setMilestoneDate(new Date());
        workOrderVo.setCreateDate(new Date());
        workOrderVo.setReqCompleteDate(new Date());
        workOrderVo.setFlowStage("FlOW_START");
        workOrderVo.setStatus(OaFlows.valueOf(workOrderVo.getFlowStage()).getStatus());
        workOrderVo.setFlowStageName(OaFlows.valueOf(workOrderVo.getFlowStage()).getName());

//        workOrderMapper.insert(workOrderVO);

//        Page<WorkOrderVO> page =  workOrderService.listTodo("todo","1",1,20);
//
//        LOGGER.info(mapper.writeValueAsString(page));

//        User u = userMapper.findByName("AAA");
//        Assert.assertEquals(20, u.getAge().intValue());
    }

}
