package com.yioa.car.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.yioa.car.domain.CarSendOrderVo;
import com.yioa.car.mapper.CarSendOrderMapper;
import com.yioa.common.exception.YiException;
import com.yioa.common.util.CommonUtil;
import com.yioa.common.util.PageUtil;
import com.yioa.common.util.SysErrorCnst;
import com.yioa.ssm.domain.FlowCustParam;
import com.yioa.ssm.service.FlowService;
import com.yioa.ssm.util.CarEvents;
import com.yioa.ssm.util.FlowUtil;
import com.yioa.ssm.util.OaFlows;
import com.yioa.sys.domain.SysRole;
import com.yioa.sys.mapper.SysUserMapper;

/**
 * Created by tao on 2017-06-04.
 */

@Service
public class CarSendService extends ServiceImpl<CarSendOrderMapper, CarSendOrderVo> {
	
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private FlowService flowService;


    @Autowired
    private CarSendOrderMapper carSendOrderMapper;

    public String orderSubmit(String userId, String userName, CarSendOrderVo carSendOrderVo) throws YiException {

//        String currentFlowStage = OaFlows.FlOW_START.name();
//
//        //存储workorder
//        if (StringUtils.isEmpty(carSendOrderVo.getCarSendOrderId())) {
//            carSendOrderVo.setCarSendOrderId(CommonUtil.getUUID());
//        }
//        carSendOrderVo.setCarSendOrderCode(CommonUtil.genOrderCode("C"));
//        carSendOrderVo.setFlowStage(currentFlowStage);
//
//        //存储实际的名字而不是登录名
//        SysUser user = sysUserMapper.selectById(userId);
//
//        carSendOrderVo.setCreateBy(user.getName());
//        carSendOrderVo.setCreateById(userId);
//        carSendOrderVo.setCreateDate(new Date());
//        carSendOrderVo.setStatus(CarSendOrderStatus.STATUS_SUBMITTED.getName());
//
////        List<SysUser> userList = sysUserService.getUserById(ImmutableList.copyOf(StringUtils.split(carSendOrderVo.getFeedbackId(), ",")));
////        List<String> userNameList = Lists.transform(userList, new Function<SysUser, String>() {
////            @Override
////            public String apply(SysUser user) {
////                return user.getLoginName();
////            }
////        });
////        carSendOrderVo.setFeedbackName(StringUtils.join(userNameList, ","));
//        boolean bool = carSendOrderVo.insert();
//
//        if (!bool) {
//            throw new RuntimeException("存储派车工单错误");
//        }

        if (StringUtils.isEmpty(carSendOrderVo.getCarSendOrderId())) {
            String tmp = this.saveOrder(userId, userName, carSendOrderVo);
            if (!tmp.equalsIgnoreCase("1")) {
                throw new RuntimeException("存储工单错误");
            }
        }else{
            carSendOrderVo.setCreateDate(new Date());
            carSendOrderVo.setStatus(OaFlows.FlOW_START.getName());
            boolean bool = this.updateById(carSendOrderVo);
            if (!bool) {
                throw new RuntimeException("存储工单错误");
            }
        }
        //启动流程
        FlowCustParam flowCustParam = new FlowCustParam(carSendOrderVo.getCarSendOrderId(), userName, userId, null, null, FlowUtil.ORDER_CAR);

        boolean suc = flowService.subFlow(flowCustParam, OaFlows.FlOW_START.name(), CarEvents.CAR_SUBMIT.name());

        if (!suc) {
            throw new YiException(SysErrorCnst.FLOW_RUN_ERR, SysErrorCnst.MSG_FLOW_RUN_ERR);
        }

        return "1";
    }


    public String saveOrder(String userId, String userName, CarSendOrderVo carSendOrderVo) throws YiException {

        String currentFlowStage = OaFlows.FlOW_START.name();

        //存储workorder
        if (StringUtils.isEmpty(carSendOrderVo.getCarSendOrderId())) {
            carSendOrderVo.setCarSendOrderId(CommonUtil.getUUID());
        }
        carSendOrderVo.setCarSendOrderCode(CommonUtil.genOrderCode("C"));
        carSendOrderVo.setFlowStage(currentFlowStage);

        //存储实际的名字而不是登录名
        carSendOrderVo.setCreateBy(userName);
        carSendOrderVo.setCreateById(userId);
        carSendOrderVo.setCreateDate(new Date());
        carSendOrderVo.setStatus("草稿");
        boolean bool = carSendOrderVo.insert();

        if (!bool) {
            throw new RuntimeException("存储派车工单错误");
        }
        return "1";
    }



    private List<String> getRoleIdStrByUserId(String userId) {
        Map<String, Object> map = ImmutableMap.of("userId", userId);
        List<SysRole> reList = this.sysUserMapper.queryRoleByUserId(map);
        List<String> idList = reList.stream().map(SysRole::getId).collect(Collectors.toList());
        return idList;
    }

    public Page<CarSendOrderVo> listTodo(String status, String userId, int current, int size,String keyword) {
        int offset = PageUtil.getOffset(current, size);

        List<String> idList = this.getRoleIdStrByUserId(userId);
        Map<String, Object> map = Maps.newHashMap();
        map.put("user_id", userId);
        map.put("offset", offset);
        map.put("size", size);
        map.put("roleIdStr", "," + StringUtils.join(idList, ",") + ",");
        map.put("status", status);

        //搜索条件
        if (StringUtils.isNotEmpty(keyword)) {
            map.put("keyword", "%" + keyword + "%");
        }

//        Map<String, Object> map  = ImmutableMap.of("status", status, column, userId, "offset", offset, "size", size, "roleIdStr", "," + StringUtils.join(idList, ",") + ",");;

        List<CarSendOrderVo> list = this.carSendOrderMapper.selectOrderByStatusAndUserId(map);
        Page<CarSendOrderVo> page = new Page<>();
        page.setRecords(list);
        page.setCurrent(current);
        page.setSize(size);
        page.setCondition(map);

        int total = this.carSendOrderMapper.selectOrderCountByStatusAndUserId(map);
        page.setTotal(total);

        return page;
    }
    

    public List<CarSendOrderVo> listTodo(String status, String keyword) {
        Map<String, Object> map = Maps.newHashMap();
       
        map.put("status", status);

        //搜索条件
        if (StringUtils.isNotEmpty(keyword)) {
            map.put("keyword", "%" + keyword + "%");
        }

//        Map<String, Object> map  = ImmutableMap.of("status", status, column, userId, "offset", offset, "size", size, "roleIdStr", "," + StringUtils.join(idList, ",") + ",");;

        List<CarSendOrderVo> list = this.carSendOrderMapper.selectOrderByStatus(map);
        return list;
    }


    public Page<CarSendOrderVo> mySubmitOrderList(String status, String userId, int current, int size,String keyword) {
//        String column = "create_by_id";
        int offset = PageUtil.getOffset(current, size);

        Map<String, Object> map = Maps.newHashMap();
        map.put("create_by_id", userId);
        map.put("offset", offset);
        map.put("size", size);
        map.put("status", status);
        //搜索条件
        if (StringUtils.isNotEmpty(keyword)) {
            map.put("keyword", "%" + keyword + "%");
        }

//        Map<String, Object> map = map = ImmutableMap.of("status", status, column, userId, "offset", offset,"size", size );

        List<CarSendOrderVo> list = this.carSendOrderMapper.selectMyOrderByStatus(map);

        Page<CarSendOrderVo> page = new Page<>();
        page.setRecords(list);
        page.setCurrent(current);
        page.setSize(size);
        page.setCondition(map);

        int total = this.carSendOrderMapper.selectMyOrderCountByStatus(map);
        page.setTotal(total);

        return page;

    }


    @RequestMapping(value = "/order/detail/{orderid}", method = RequestMethod.GET)
    public CarSendOrderVo workorderDetail(@PathVariable String orderid, HttpServletRequest request) throws YiException {

        CarSendOrderVo carSendOrderVo = this.carSendOrderMapper.selectById(orderid);

        return carSendOrderVo;
    }


    public boolean canDeal(String orderId, String userId){
        List<String> idList = this.getRoleIdStrByUserId(userId);

        Map<String, Object> map = Maps.newHashMap();
//        map.put("likeUserId", "%," + userId + ",%");
        map.put("orderId",orderId);
        map.put("user_id", userId);

        map.put("roleIdStr", "," + StringUtils.join(idList, ",") + ",");


        int total = this.carSendOrderMapper.selectCanDealCount(map);

        return total > 0;

    }

}
