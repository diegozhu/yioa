package com.yioa.market.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.yioa.common.exception.YiException;
import com.yioa.common.util.CommonUtil;
import com.yioa.market.domain.MarketOrderVo;
import com.yioa.market.service.MarketSendService;
import com.yioa.sys.domain.SysUser;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Created by liangpengcheng on 2018/5/5.
 */
@RestController
@RequestMapping("/t_market_order")
public class MarketController {
    @Autowired
    private MarketSendService marketSendService;
    private static final Logger logger = LoggerFactory.getLogger(MarketController.class);
    
    @RequestMapping(value = "/submit",method = RequestMethod.POST)
    public String marketOrderSubmit(MarketOrderVo marketVo,HttpServletRequest request) throws YiException {
    	logger.debug("");
        String userId = CommonUtil.getUserIdFromSession(request);
        SysUser user = CommonUtil.getUserLoginInfoFromSession(request);
        
        return marketSendService.orderSubmit(userId,user.getName(),marketVo);
    }

    @ApiOperation(value = "营销宝查询", notes = "返回参数包括：营销活动 营销用户 经纬度信息 开始时间 结束时间。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "marketAreaName", value = "区域", required = true, dataType = "String"),
            @ApiImplicitParam(name = "planLeaveDate", value = "开始时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "planBackDate", value = "结束时间", required = true, dataType = "String"),

    })
    @RequestMapping(value = "/queryMarket/{marketAreaName}/{planLeaveDate}/{planBackDate}",method = RequestMethod.GET)
    public Page<MarketOrderVo> queryMarket(@PathVariable String marketAreaName, @PathVariable String planLeaveDate,@PathVariable String planBackDate,HttpServletRequest request) throws YiException{
        String userId = CommonUtil.getUserIdFromSession(request);
        Page<MarketOrderVo> list = marketSendService.qryMarketList(userId,marketAreaName,planLeaveDate,planBackDate);

        System.out.println("区域是" + marketAreaName + planLeaveDate + planBackDate);
        return list;
    }

    @ApiOperation(value = "待办工单列表接口", notes = "此接口有一系列的查询,用status区分三个状态：处理中，已回单，已归档;返回参数包括：工单ID，工单名称，工单类型，派单人，派单时间等。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "工单状态", required = true, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页步长", required = false, dataType = "String")
    })
    @RequestMapping(value = "/todo/{status}",method = RequestMethod.GET)
    public Page<MarketOrderVo> listTodo(@PathVariable String status, @RequestParam(required = false, defaultValue = "1") int current,
                                        @RequestParam(required = false, defaultValue = "20") int size, @RequestParam(required = false, defaultValue = "") String keyword, HttpServletRequest request) throws YiException {
        String userId = CommonUtil.getUserIdFromSession(request);
        Page<MarketOrderVo> list = marketSendService.listTodo(status,userId,current,size,keyword);

        return list;
    }

    @ApiOperation(value = "工单详情", notes = "返回参数包括：工单ID，工单内容，工单名称，回单人，派单人，协办人，派单时间，回单时间，阶段回单时间，要求完成时间，要求阶段反馈时间，回单内容，阶段回单内容等。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workorderid", value = "工单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/detail/{workorderid}", method = RequestMethod.GET)
    public MarketOrderVo workorderDetail(@PathVariable String workorderid, HttpServletRequest request) throws YiException {

//        String userId = CommonUtil.getUserIdFromSession(request);

        MarketOrderVo workOrderVo = marketSendService.selectById(workorderid);

        return workOrderVo;
    }
    @ApiOperation(value = "是否具备制定订单的流程处理权限", notes = "是否具备制定订单的流程处理权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "工单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/workorder/candeal/{orderId}", method = RequestMethod.GET)
    public String canDeal(@PathVariable String orderId, HttpServletRequest request) throws YiException {
        String userId = CommonUtil.getUserIdFromSession(request);

        boolean bool = this.marketSendService.canDeal(orderId, userId);
        return bool ? "1" : "-1";
    }














}
