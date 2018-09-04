package com.yioa.core.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.yioa.car.domain.CarSendOrderVo;
import com.yioa.car.mapper.CarSendOrderMapper;
import com.yioa.car.service.CarSendService;
import com.yioa.common.exception.YiException;
import com.yioa.common.util.CommonUtil;
import com.yioa.common.util.PageUtil;
import com.yioa.core.mapper.WatchOrderMapper;
import com.yioa.market.domain.MarketOrderVo;
import com.yioa.market.mapper.MarketOrderMapper;
import com.yioa.oa.domain.WatchOrderVo;
import com.yioa.oa.domain.WorkOrderVo;
import com.yioa.oa.mapper.WorkOrderMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by tao on 2017-06-29.
 */

@RestController
@RequestMapping("/oa/core")
public class CoreController {

    @Autowired
    private WatchOrderMapper watchOrderMapper;


    @Autowired
    private CarSendOrderMapper carSendOrderMapper;

    @Autowired
    private WorkOrderMapper workOrderMapper;

    @Autowired
    private MarketOrderMapper marketOrderMapper;


    @ApiOperation(value = "check", notes = "登陆检查")
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String check(HttpServletRequest request) throws YiException {
        return "1";
    }


    @ApiOperation(value = "关注", notes = "关注某工单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "工单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/watch/{orderId}", method = RequestMethod.POST)
    public String watch(@PathVariable String orderId, HttpServletRequest request) throws YiException {

        String userId = CommonUtil.getUserIdFromSession(request);

        WatchOrderVo watchOrderVo = new WatchOrderVo();

        WatchOrderVo tmp = watchOrderVo.selectOne(new EntityWrapper().eq("order_id", orderId).eq("user_id", userId).eq("delete_state", "0"));
        if (null == tmp) {
            watchOrderVo.setCreateDate(new Date());
            watchOrderVo.setDeleteState("0");
            watchOrderVo.setOrderId(orderId);
            watchOrderVo.setUserId(userId);
            watchOrderVo.setWatchId(CommonUtil.getUUID());
            watchOrderVo.insert();
        }
        return "1";
    }

    @ApiOperation(value = "取消关注", notes = "取消关注某工单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "工单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/unwatch/{orderId}", method = RequestMethod.POST)
    public String unwatch(@PathVariable String orderId, HttpServletRequest request) throws YiException {

        String userId = CommonUtil.getUserIdFromSession(request);

        WatchOrderVo watchOrderVo = new WatchOrderVo();
        watchOrderVo = watchOrderVo.selectOne(new EntityWrapper().eq("order_id", orderId).eq("user_id", userId).eq("delete_state", "0"));
        if(watchOrderVo != null){
            watchOrderVo.setDeleteState("1");
            watchOrderVo.setDeleteDate(new Date());
            watchOrderVo.updateById();
        }
        return "1";
    }

    @ApiOperation(value = "关注的OA工单", notes = "取消关注某工单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "工单状态", required = true, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "keyword,搜索关键字", required = true, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页步长", required = false, dataType = "String")
    })
    @RequestMapping(value = "/watchlist/oa/{status}", method = RequestMethod.GET)
    public Page<WorkOrderVo> watchListOa(@PathVariable String status, @RequestParam(required = false, defaultValue = "1") int current,
                                         @RequestParam(required = false, defaultValue = "20") int size,
                                         @RequestParam(required = false, defaultValue = "") String keyword,
                                         HttpServletRequest request) throws YiException {

        Map<String, Object> map = this.makeCondition(status, current, size, keyword, request);

        List<WorkOrderVo> list = this.workOrderMapper.selectWatchList(map);
        List<MarketOrderVo> list2 = this.marketOrderMapper.selectWatchList(map);
        List<CarSendOrderVo> carList = this.carSendOrderMapper.selectWatchList(map);
        List<WorkOrderVo> list3 = new ArrayList<>();
        WorkOrderVo vo;

        //事务单
        for(int i = 0;i < list.size();i ++){
            vo = new WorkOrderVo();
            vo.setWorkOrderId(list.get(i).getWorkOrderId());
            vo.setFlowInfoId(list.get(i).getFlowInfoId());
            vo.setFlowStage(list.get(i).getWorkOrderSubject());
            vo.setWorkOrderSubject(list.get(i).getWorkOrderSubject());
            vo.setStatus(list.get(i).getStatus());
            vo.setWorkOrderContent(list.get(i).getWorkOrderContent());
            vo.setWorkOrderCode(list.get(i).getWorkOrderCode());
            vo.setCreateDate(list.get(i).getCreateDate());
            vo.setWatched(list.get(i).getWatched());
            vo.setOrderType("1");
            vo.setOrderIcon("icon_thing");
            list3.add(vo);
        }
//        //营销单
        for(int i = 0;i < list2.size();i++){
            vo = new WorkOrderVo();
            vo.setWorkOrderId(list2.get(i).getMarket_send_order_id());
            vo.setFlowInfoId(list2.get(i).getFlowInfoId());
            vo.setFlowStage(list2.get(i).getFlowStage());
            vo.setWorkOrderSubject(list2.get(i).getWork_order_subject());
            vo.setStatus(list2.get(i).getStatus());
            vo.setWatched(list2.get(i).getWatched());
            vo.setWorkOrderContent(list2.get(i).getWo_order_content());
            vo.setWorkOrderCode(list2.get(i).getMarket_send_order_code());
            vo.setCreateDate(list2.get(i).getCreateDate());
            vo.setOrderType("2");
            vo.setOrderIcon("icon_market");
            list3.add(vo);
        }
        //营销单
        for(int i = 0;i < carList.size();i++){
            vo = new WorkOrderVo();
            vo.setWorkOrderId(carList.get(i).getCarSendOrderId());
            vo.setFlowInfoId(carList.get(i).getFlowInfoId());
            vo.setFlowStage(carList.get(i).getFlowStage());
            vo.setWorkOrderSubject(carList.get(i).getCarSendOrderSubject());
            vo.setStatus(carList.get(i).getStatus());
            vo.setWorkOrderContent(carList.get(i).getCarSendOrderContent());
            vo.setWorkOrderCode(carList.get(i).getCarSendOrderContent());
            vo.setCreateDate(carList.get(i).getCreateDate());
            vo.setOrderType("3");
            vo.setOrderIcon("icon_car");
            list3.add(vo);
        }

        int total = this.workOrderMapper.selectWatchListCount(map) + this.marketOrderMapper.selectWatchListCount(map);

        Page<WorkOrderVo> page = new Page<>();
        page.setRecords(list3);
        page.setCurrent(current);
        page.setSize(size);
        page.setCondition(map);
        page.setTotal(total);

        return page;
    }


    private Map<String, Object> makeCondition(String status, int current,
                                              int size,
                                              String keyword, HttpServletRequest request) throws YiException {
        String userId = CommonUtil.getUserIdFromSession(request);
        int offset = PageUtil.getOffset(current, size);

        Map<String, Object> map = Maps.newHashMap();
        map.put("userId", userId);
        map.put("offset", offset);
        map.put("size", size);
        map.put("status", status);
        //搜索条件
        if (StringUtils.isNotEmpty(keyword)) {
            map.put("keyword", "%" + keyword + "%");
        }
        return map;
    }


    @ApiOperation(value = "关注的派车工单", notes = "取消关注某工单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "工单状态", required = true, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "keyword,搜索关键字", required = true, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页步长", required = false, dataType = "String")
    })
    @RequestMapping(value = "/watchlist/car/{status}", method = RequestMethod.GET)
    public Page<CarSendOrderVo> watchListCar(@PathVariable String status, @RequestParam(required = false, defaultValue = "1") int current,
                                             @RequestParam(required = false, defaultValue = "20") int size,
                                             @RequestParam(required = false, defaultValue = "") String keyword,
                                             HttpServletRequest request) throws YiException {
        Map<String, Object> map = this.makeCondition(status, current, size, keyword, request);

//        Map<String, Object> map = ImmutableMap.of("userId", userId, "offset", offset, "size", size, "status", status);
        List<CarSendOrderVo> list = this.carSendOrderMapper.selectWatchList(map);

        int total = this.carSendOrderMapper.selectWatchListCount(map);

        Page<CarSendOrderVo> page = new Page<>();
        page.setRecords(list);
        page.setCurrent(current);
        page.setSize(size);
        page.setCondition(map);
        page.setTotal(total);

        return page;
    }

}
