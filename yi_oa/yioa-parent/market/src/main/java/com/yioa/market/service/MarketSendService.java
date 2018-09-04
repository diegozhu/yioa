package com.yioa.market.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.collections.Maps;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.yioa.common.exception.YiException;
import com.yioa.common.util.CommonUtil;
import com.yioa.common.util.PageUtil;
import com.yioa.common.util.SysErrorCnst;
import com.yioa.market.domain.MarketOrderVo;
import com.yioa.market.mapper.MarketOrderMapper;
import com.yioa.ssm.domain.FlowCustParam;
import com.yioa.ssm.domain.FlowHandlerVo;
import com.yioa.ssm.service.FlowService;
import com.yioa.ssm.util.FlowUtil;
import com.yioa.ssm.util.MarketEvents;
import com.yioa.ssm.util.MarketFlows;
import com.yioa.ssm.util.OaFlows;
import com.yioa.sys.domain.SysRole;
import com.yioa.sys.domain.SysUser;
import com.yioa.sys.mapper.SysUserMapper;

/**
 * Created by liangpengcheng on 2018/5/5.
 */
@Service
public class MarketSendService extends ServiceImpl<MarketOrderMapper, MarketOrderVo> {

    @Autowired
    private MarketOrderMapper marketOrderMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private FlowService flowService;

    public String orderSubmit(String userId,String userName,MarketOrderVo vo) throws YiException{
        if(StringUtils.isEmpty(vo.getMarket_send_order_id())){
            String tmp = this.marketOrderSave(userId,userName,vo);
            if(!tmp.equalsIgnoreCase("1")){
                throw new RuntimeException("存储工单错误");
            }
        }else{
            vo.setCreateDate(new Date());
            vo.setStatus(OaFlows.FlOW_START.getName());
            boolean bool = this.updateById(vo);
            if(!bool){
                throw new RuntimeException("存储工单错误");
            }
        }

        FlowHandlerVo flowHandlerVo = new FlowHandlerVo();
        flowHandlerVo.setHandlerById(vo.getPassenger());
        flowHandlerVo.setHandlerBy(vo.getPassenger_name());
        flowHandlerVo.setHandlerByType(FlowUtil.USER_HANDLER_TYPE);
        //启动流程
        FlowCustParam flowCustParam = new FlowCustParam(vo.getMarket_send_order_id(),userName,userId,null,flowHandlerVo, FlowUtil.ORDER_MARKET);
        boolean suc = flowService.subFlow(flowCustParam, MarketFlows.MARKET_FlOW_START.name(), MarketEvents.MARKET_SUBMIT.name());
        if(!suc){
            throw new YiException(SysErrorCnst.FLOW_RUN_ERR,SysErrorCnst.MSG_FLOW_RUN_ERR);
        }
        return "1";
    }

    public String marketOrderSave(String userId,String userName,MarketOrderVo vo) throws YiException{
        String currentFlowState = OaFlows.FlOW_START.name();
        //存储MarketOrder
        if(StringUtils.isEmpty(vo.getMarket_send_order_id())){
            vo.setMarket_send_order_id(CommonUtil.getUUID());
        }
        //任务编码规则 前缀0+日期+序号
        vo.setMarket_send_order_code(CommonUtil.genOrderCode("O"));
        vo.setFlowStage(currentFlowState);
        vo.setFlowStageName(OaFlows.valueOf(currentFlowState).getName());
        SysUser user = sysUserMapper.selectById(userId);
        vo.setCreateBy(user.getName());
        vo.setCreateById(userId);
        vo.setCreateDate(new Date());
        vo.setStatus("草稿");

        boolean bool = this.insert(vo);
        if(!bool){
            throw new RuntimeException("存储工单错误");
        }
        return "1";
    }
    

    public Page<MarketOrderVo> listTodo(String status, String userId, int current, int size, String keyword){
        int offset = PageUtil.getOffset(current,size);
        List<String> idList = this.getRoleIdStrByUserId(userId);
        Map<String,Object> map = Maps.newHashMap();
        map.put("likeUserId", "%," + userId + ",%");
        map.put("userId", userId);
        map.put("offset", offset);
        map.put("size", size);
        map.put("roleIdStr", "," + StringUtils.join(idList, ",") + ",");
        map.put("status", status);
        //搜索条件
        if (StringUtils.isNotEmpty(keyword)) {
            map.put("keyword", "%" + keyword + "%");
        }

        List<MarketOrderVo> list = this.marketOrderMapper.selectMarketOrderByStatusAndUserId(map);
        int total = this.marketOrderMapper.selectMarketOrderCountByStatusAndUserId(map);
        Page<MarketOrderVo> page = new Page<>();
        page.setRecords(list);
        page.setCurrent(current);
        page.setSize(size);
        page.setCondition(map);
        page.setTotal(total);

        return page;
    }
    

    public List<MarketOrderVo> listTodo(String status, String keyword){
        Map<String,Object> map = Maps.newHashMap();
        map.put("status", status);
        //搜索条件
        if (StringUtils.isNotEmpty(keyword)) {
            map.put("keyword", "%" + keyword + "%");
        }

        List<MarketOrderVo> list = this.marketOrderMapper.selectMarketOrderByStatus(map);
        return list;
    }


    private List<String> getRoleIdStrByUserId(String userId) {
        Map<String, Object> map = ImmutableMap.of("userId", userId);
        List<SysRole> reList = this.sysUserMapper.queryRoleByUserId(map);
        List<String> idList = reList.stream().map(SysRole::getId).collect(Collectors.toList());
        return idList;
    }

    public boolean canDeal(String workOrderId, String userId){
        List<String> idList = this.getRoleIdStrByUserId(userId);

        Map<String,Object> map = Maps.newHashMap();
        map.put("workOrderId", workOrderId);
        map.put("userId", userId);
        map.put("roleIdStr", "," + StringUtils.join(idList, ",") + ",");
        int total = this.marketOrderMapper.selectCanDealCount(map);
        return total > 0;
    }

    /**
     * 根据时间和日期查询营销单
     * @param marketAreaName
     * @param planLeaveDate
     * @param planBackDate
     * @return
     */
    public Page<MarketOrderVo> qryMarketList(String userId,String marketAreaName,String planLeaveDate,String planBackDate){
        Map<String, Object> map = Maps.newHashMap();
        map.put("marketAreaName",marketAreaName);
        map.put("planLeaveDate",planLeaveDate);
        map.put("planBackDate",planBackDate);

        List<MarketOrderVo> list = this.marketOrderMapper.qryMarketList(map);
        int total = this.marketOrderMapper.qryMarketListCount(map);
        Page<MarketOrderVo> page = new Page<>();
        page.setRecords(list);
        page.setCondition(map);
        page.setTotal(total);
        return page;
    }

    public Page<MarketOrderVo> mySubmitOrderList(String status, String userId, int current, int size,String keyword) {
//        String column = "create_by_id";
        int offset = PageUtil.getOffset(current, size);

        Map<String, Object> map = com.google.common.collect.Maps.newHashMap();
        map.put("create_by_id", userId);
        map.put("offset", offset);
        map.put("size", size);
        map.put("status", status);
        //搜索条件
        if (StringUtils.isNotEmpty(keyword)) {
            map.put("keyword", "%" + keyword + "%");
        }

//        Map<String, Object> map = map = ImmutableMap.of("status", status, column, userId, "offset", offset,"size", size );

        List<MarketOrderVo> list = this.marketOrderMapper.selectMyOrderByStatus(map);

        Page<MarketOrderVo> page = new Page<>();
        page.setRecords(list);
        page.setCurrent(current);
        page.setSize(size);
        page.setCondition(map);

        int total = this.marketOrderMapper.selectMyOrderCountByStatus(map);
        page.setTotal(total);

        return page;

    }


}
