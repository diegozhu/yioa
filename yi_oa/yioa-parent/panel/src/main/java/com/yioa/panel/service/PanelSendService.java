package com.yioa.panel.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.yioa.common.exception.YiException;
import com.yioa.common.util.CommonUtil;
import com.yioa.common.util.PageUtil;
import com.yioa.common.util.SysErrorCnst;
import com.yioa.panel.domain.PanelVo;
import com.yioa.panel.mapper.PanelOrderMapper;
import com.yioa.ssm.domain.FlowCustParam;
import com.yioa.ssm.domain.FlowHandlerVo;
import com.yioa.ssm.service.FlowService;
import com.yioa.ssm.util.*;
import com.yioa.sys.domain.SysRole;
import com.yioa.sys.domain.SysUser;
import com.yioa.sys.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.testng.collections.Maps;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by liangpengcheng on 2018/4/2.
 */
@Service
public class PanelSendService extends ServiceImpl<PanelOrderMapper, PanelVo>{

    @Autowired
    private PanelOrderMapper panelOrderMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private FlowService flowService;
    public String orderSubmit(String userId,String userName,PanelVo vo) throws YiException{
        if(StringUtils.isEmpty(vo.getWorkOrderId())){
            String tmp = this.panelorderSave(userId,userName,vo);
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
        flowHandlerVo.setHandlerById(vo.getOrganiser());
        flowHandlerVo.setHandlerBy(vo.getOrganiserName());
        flowHandlerVo.setHandlerByType(FlowUtil.USER_HANDLER_TYPE);
        //启动流程
        FlowCustParam flowCustParam = new FlowCustParam(vo.getWorkOrderId(),userName,userId,null,flowHandlerVo,FlowUtil.ORDER_PANEL);
        boolean suc = flowService.subFlow(flowCustParam, OaFlows.FlOW_START.name(), PanelEvents.PANEL_SUBMIT.name());
        if(!suc){
            throw new YiException(SysErrorCnst.FLOW_RUN_ERR,SysErrorCnst.MSG_FLOW_RUN_ERR);
        }
        return "1";
    }

    public String panelorderSave(String userId,String userName,PanelVo panelVo) throws YiException{
        String currentFlowState = OaFlows.FlOW_START.name();
        //存储panelOrder
        if(StringUtils.isEmpty(panelVo.getWorkOrderId())){
            panelVo.setWorkOrderId(CommonUtil.getUUID());
        }
        //任务编码规则 前缀0+日期+序号
        panelVo.setWorkOrderCode(CommonUtil.genOrderCode("O"));
        panelVo.setFlowStage(currentFlowState);
        panelVo.setFlowStageName(OaFlows.valueOf(currentFlowState).getName());
        //存储实际的名字而不是登录名
        SysUser user = sysUserMapper.selectById(userId);
        panelVo.setCreateBy(user.getName());
        panelVo.setCreateById(userId);
        panelVo.setCreateDate(new Date());
        panelVo.setStatus("草稿");

        boolean bool = this.insert(panelVo);
        if(!bool){
            throw new RuntimeException("存储工单错误");
        }
        return "1";

    }

    public Page<PanelVo> listSave(String status,String userId, int current, int size, String keyword){
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
        List<PanelVo> list = this.panelOrderMapper.selectPanelOrderByStatusAndUserId(map);
        int total = this.panelOrderMapper.selectPanelOrderCountByStatusAndUserId(map);

        Page<PanelVo> page = new Page<>();
        page.setRecords(list);
        page.setCurrent(current);
        page.setSize(size);
        page.setCondition(map);
        page.setTotal(total);

        return page;
    }

    private List<String> getRoleIdStrByUserId(String userId) {
        Map<String, Object> map = ImmutableMap.of("userId", userId);
        List<SysRole> reList = this.sysUserMapper.queryRoleByUserId(map);
        List<String> idList = reList.stream().map(SysRole::getId).collect(Collectors.toList());
        return idList;
    }


}
