package com.yioa.oa.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.ImmutableMap;
import com.yioa.common.exception.YiException;
import com.yioa.common.util.CommonUtil;
import com.yioa.common.util.PageUtil;
import  com.yioa.oa.cfg.OaStateMachineConfig.OaState;
import com.yioa.oa.domain.WorkOrderVo;
import com.yioa.oa.mapper.WorkOrderMapper;
import com.yioa.oa.service.base.impl.EoaBaseService;
import com.yioa.ssm.domain.FlowInfoVo;
import com.yioa.ssm.util.FlowUtil;

/**
 * Created by tao on 2017-05-23.
 */
@Service
public class WorkOrderService extends EoaBaseService<WorkOrderMapper, WorkOrderVo> {

    private static final Logger logger = LoggerFactory.getLogger(WorkOrderService.class);

    @Autowired
    private WorkOrderMapper workOrderMapper;

    public Page<WorkOrderVo> listTodo(String status, String userId, int current, int size, String keyword) {
    	logger.debug("");
    	
    	String sql = "select * from t_work_order where "
    			+ "(((status = 'START' or status = 'MILESTONE') and organiser = '" + userId + "') or (status = 'COMPLETE' and create_by_id  = '" + userId + "')) ";
    	if(StringUtils.isNotBlank(keyword)) {
    		keyword = keyword.trim();
    		sql += " and (  1 = 2 ";
    		for(String f : Arrays.asList("work_order_code", "organiser_name", "co_organiser_name", "work_order_subject", "work_order_content", "create_by")) {
    			sql += " or " + f + " like '%" + keyword+ "%' ";
    		}
    		sql += ")";
    	}
    	if(StringUtils.isNotBlank(status)) {
    		status = status.trim();
    		sql += " and status = '" + status + "'";
    	}
    	Page<WorkOrderVo> page = execSqlPage(sql, current, size, null, WorkOrderVo.class);
        return page;
    }
    

    public Page<WorkOrderVo> listSave(String userId, int current, int size, String keyword) {
//        int offset = PageUtil.getOffset(current, size);
//
//        Map<String, Object> map = Maps.newHashMap();
//
//        map.put("userId", userId);
//        map.put("offset", offset);
//        map.put("size", size);
//        map.put("status", "草稿");
//
//        //搜索条件
//        if (StringUtils.isNotEmpty(keyword)) {
//            map.put("keyword", "%" + keyword + "%");
//        }
//        List<WorkOrderVo> list = this.workOrderMapper.selectList(new EntityWrapper<WorkOrderVo>());
//        int total = this.workOrderMapper.listSaveCount(map);
//        Page<WorkOrderVo> page = new Page<>();
//        page.setRecords(list);
//        page.setCurrent(current);
//        page.setSize(size);
//        page.setCondition(map);
//        page.setTotal(total);
        Page<WorkOrderVo> page = this.selectPage(new Page<WorkOrderVo>(current, size), new EntityWrapper<WorkOrderVo>().eq("status", "草稿").eq("create_by_id", userId));
        return page;
    }

    public Page<WorkOrderVo> mySubmitOrderList(String status, String userId, int current, int size, String keyword) {

        String column = "create_by_id";
        int offset = PageUtil.getOffset(current, size);
        Map<String, Object> map = null;

        if (StringUtils.isNotEmpty(keyword)) {
            map = ImmutableMap.of("status", status, column, userId, "offset", offset, "size", size, "keyword", "%" + keyword + "%");
        } else {
            map = ImmutableMap.of(column, userId, "offset", offset, "size", size);
        }
        
        if(StringUtils.isNotBlank(status)) {
        	map.put("status", status);
        }

        List<WorkOrderVo> list = this.workOrderMapper.selectMyWorkOrderByStatus(map);
//        for( WorkOrderVo workOrderVo : list){
//            if(!"1".equalsIgnoreCase(workOrderVo.getEditable())){
//                workOrderVo.setEditable(null);
//            }
//        }
        int total = this.workOrderMapper.selectMyWorkOrderCountByStatus(map);

        Page<WorkOrderVo> page = new Page<>();
        page.setRecords(list);
        page.setCurrent(current);
        page.setSize(size);
        page.setCondition(map);
        page.setTotal(total);

        return page;

    }

    /**
     * 工单签到
     * 本方法只针对营销单
     *
     * @param userId
     * @param userName
     * @param workOrderId
     * @param flowInfoVo
     * @return
     * @throws YiException
     */
    public boolean sign(String userId, String userName, String workOrderId, FlowInfoVo flowInfoVo) throws YiException {

		flowInfoVo.setFlowInfoId(CommonUtil.getUUID());
		flowInfoVo.setCreateDate(new Date());
		flowInfoVo.setAction(FlowUtil.SIGN_FLOW_STAGE);
		flowInfoVo.setWorkOrderId(workOrderId);
		flowInfoVo.setCreateDate(new Date());
		return flowInfoVo.insert();
    }

    public String saveSignInfo(String workOrderId, String address, String longitude, String lantitude) throws YiException {
        Map<String,Object> params = new HashMap<>();
        params.put("workOrderId",workOrderId);
        params.put("address",address);
        params.put("longitude",longitude);
        params.put("lantitude",lantitude);
        params.put("create_date",new Date());
        this.workOrderMapper.saveSignInfo(params);
        return "1";
    }


    /**
     * 提交工单，启动流程
     * <p>
     * 只有本环节是先插入工单，再走流程的
     *
     * @param userId
     * @param userName
     * @param workOrderVo
     * @return
     * @throws YiException
     */
    public String workorderSubmit(String userId, String userName, WorkOrderVo workOrderVo) throws YiException {

        if (StringUtils.isEmpty(workOrderVo.getWorkOrderId())) {
            String tmp = this.workorderSave(userId, userName, workOrderVo);
            if (!tmp.equalsIgnoreCase("1")) {
                throw new RuntimeException("存储工单错误");
            }
        }else{
            workOrderVo.setCreateDate(new Date());
            workOrderVo.setStatus(OaState.START.toString());
            workOrderVo.setOrderType("1");
            workOrderVo.setOrderIcon("icon_thing");
            boolean bool = this.updateById(workOrderVo);
            if (!bool) {
                throw new RuntimeException("存储工单错误");
            }
        }
        return "1";
    }


    /**
     * 存储草稿
     *
     * @param userId
     * @param userName
     * @param workOrderVo
     * @return
     * @throws YiException
     */
    public String workorderSave(String userId, String userName, WorkOrderVo workOrderVo) throws YiException {
        //存储workorder
        if (StringUtils.isEmpty(workOrderVo.getWorkOrderId())) {
            workOrderVo.setWorkOrderId(CommonUtil.getUUID());
        }
        //工单编码的规则，前缀O+日期+序号
        workOrderVo.setWorkOrderCode(CommonUtil.genOrderCode("O"));

        //存储实际的名字而不是登录名
        workOrderVo.setCreateBy(userName);
        workOrderVo.setCreateById(userId);
        workOrderVo.setCreateDate(new Date());
        workOrderVo.setStatus(OaState.DRAFT.toString());
        workOrderVo.setOrderType("1");
        workOrderVo.setOrderIcon("icon_thing");
        
        boolean bool = this.insert(workOrderVo);
        if (!bool) {
            throw new RuntimeException("存储工单错误");
        }
        return "1";
    }


    //    @Override
//    public String approve(String userId, String userName, String workOrderId, String pass, FlowInfoVo flowInfoVo) throws YiException {
//
//
//        FlowHandlerVo flowHandlerVo = null;
//        boolean suc = false;
//        if (pass.equalsIgnoreCase("approve")) {
//            flowHandlerVo = handlerService.getFlowHandler(
//                    OaFlows.FLOW_MILESTONE.name(),
//                    flowInfoVo.getFlowInfoId());
//        } else {
//            flowHandlerVo = handlerService.getFlowHandler(
//                    OaFlows.FLOW_DONE.name(),
//                    flowInfoVo.getFlowInfoId());
//
//        }
//        String currentFlowStage = OaFlows.FLOW_AUDIT.name();
//
//
//        return this.flowSubmit(userId, userName, workOrderId, currentFlowStage, flowHandlerVo, flowInfoVo);
//    }
//
//
//    /**
//     * 阶段回单
//     *
//     * @param userId
//     * @param userName
//     * @param workOrderId
//     * @param flowInfoVo
//     * @return
//     * @throws YiException
//     */
//    @Override
//    public String milestone(String userId, String userName, String workOrderId, FlowInfoVo flowInfoVo) throws YiException {
//        //先走流程，再改工单状态
//        FlowHandlerVo flowHandlerVo = handlerService.getFlowHandler(
//                OaFlows.FLOW_COMPLETE.name(),
//                flowInfoVo.getFlowInfoId());
//        String currentFlowStage = OaFlows.FLOW_AUDIT.name();
//        return this.flowSubmit(userId, userName, workOrderId, currentFlowStage, flowHandlerVo, flowInfoVo);
//    }
//
//
//    /**
//     * 回单，即归档，结束流程
//     *
//     * @param userId
//     * @param userName
//     * @param workOrderId
//     * @param flowInfoVo
//     * @return
//     * @throws YiException
//     */
//    @Override
//    public String completeOrder(String userId, String userName, String workOrderId, FlowInfoVo flowInfoVo) throws YiException {
//
//
//        //先走流程，再改工单状态
//        FlowHandlerVo flowHandlerVo = handlerService.getFlowHandler(
//                OaFlows.FLOW_DONE.name(),
//                flowInfoVo.getFlowInfoId());
//        String currentFlowStage = OaFlows.FLOW_COMPLETE.name();
//        return this.flowSubmit(userId, userName, workOrderId, currentFlowStage, flowHandlerVo, flowInfoVo);
//
//    }

	public List<FlowInfoVo> getOrderFlow(String workorderid) {
		List<FlowInfoVo> page = execSql("select * from t_flow_info where work_order_id = :orderId order by create_date desc ", ImmutableMap.of("orderId", workorderid), FlowInfoVo.class);
		return page;
	}


}
