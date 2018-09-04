package com.yioa.oa.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.eventbus.EventBus;
import com.xseed.sms.common.SmsEvent;
import com.yioa.car.domain.CarSendOrderVo;
import com.yioa.car.mapper.CarSendOrderMapper;
import com.yioa.car.service.CarSendService;
import com.yioa.common.exception.YiException;
import com.yioa.common.util.CommonUtil;
import com.yioa.common.util.SysErrorCnst;
import com.yioa.market.domain.MarketOrderVo;
import com.yioa.market.mapper.MarketOrderMapper;
import com.yioa.market.service.MarketSendService;
import com.yioa.oa.domain.WatchOrderVo;
import com.yioa.oa.domain.WorkOrderVo;
import com.yioa.oa.service.IWorkOrderService;
import com.yioa.ssm.domain.FlowHandlerVo;
import com.yioa.ssm.domain.FlowInfoVo;
import com.yioa.ssm.mapper.FlowInfoMapper;
import com.yioa.ssm.service.FlowService;
import com.yioa.sys.domain.SysUser;
import com.yioa.sys.mapper.SysUserMapper;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Created by tao on 2017-05-23.
 */
@RestController
@RequestMapping("/oa/workorder")
public class WorkorderController {



    private static final Logger logger = LoggerFactory.getLogger(WorkorderController.class);

    @Autowired
    private IWorkOrderService workOrderService;

    @Autowired
    private FlowInfoMapper flowInfoMapper;

    @Autowired
    private CarSendOrderMapper carSendOrderMapper;


    @Autowired
    private MarketOrderMapper marketOrderMapper;
    
    @Autowired
    private CarSendService carSendService;
    
    @Autowired
    private FlowService flowService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private MarketSendService marketSendService;

    @Autowired
    private EventBus eventBus;

    @RequestMapping(value = "/{orderType}", method = RequestMethod.GET)
    public Page<WorkOrderVo> getByOrderType(@PathVariable String status, @RequestParam(required = false, defaultValue = "1") int current,
                                      @RequestParam(required = false, defaultValue = "20") int size, @RequestParam(required = false, defaultValue = "") String keyword, HttpServletRequest request) throws YiException {

        String userId = CommonUtil.getUserIdFromSession(request);
        logger.debug("userid");
        Page<WorkOrderVo> list = workOrderService.listTodo(status, userId, current, size, keyword);
        Page<MarketOrderVo> list2 = marketSendService.listTodo(status,userId,current,size,keyword);
        Page<CarSendOrderVo> carList = carSendService.listTodo(status,userId,current,size,keyword);
        List<WorkOrderVo> list3 = new ArrayList<>();
        WorkOrderVo vo;

        //事务单
        for(int i = 0;i < list.getRecords().size();i ++){
            vo = new WorkOrderVo();
            vo.setWorkOrderId(list.getRecords().get(i).getWorkOrderId());
            vo.setFlowInfoId(list.getRecords().get(i).getFlowInfoId());
            vo.setFlowStage(list.getRecords().get(i).getWorkOrderSubject());
            vo.setWorkOrderSubject(list.getRecords().get(i).getWorkOrderSubject());
            vo.setStatus(list.getRecords().get(i).getStatus());
            vo.setWorkOrderContent(list.getRecords().get(i).getWorkOrderContent());
            vo.setWorkOrderCode(list.getRecords().get(i).getWorkOrderCode());
            vo.setCreateDate(list.getRecords().get(i).getCreateDate());
            vo.setWatched(list.getRecords().get(i).getWatched());
            vo.setOrderType("1");
            vo.setOrderIcon("icon_thing");
            list3.add(vo);
        }
        //营销单
        for(int i = 0;i < list2.getRecords().size();i++){
            vo = new WorkOrderVo();
            vo.setWorkOrderId(list2.getRecords().get(i).getMarket_send_order_id());
            vo.setFlowInfoId(list2.getRecords().get(i).getFlowInfoId());
            vo.setFlowStage(list2.getRecords().get(i).getFlowStage());
            vo.setWorkOrderSubject(list2.getRecords().get(i).getWork_order_subject());
            vo.setStatus(list2.getRecords().get(i).getStatus());
            vo.setWorkOrderContent(list2.getRecords().get(i).getWo_order_content());
            vo.setWorkOrderCode(list2.getRecords().get(i).getMarket_send_order_code());
            vo.setCreateDate(list2.getRecords().get(i).getCreateDate());
            vo.setWatched(list2.getRecords().get(i).getWatched());
            vo.setOrderType("2");
            vo.setOrderIcon("icon_market");
            list3.add(vo);
        }
        //派车单
        for(int i = 0;i < carList.getRecords().size();i++){
            vo = new WorkOrderVo();
            vo.setWorkOrderId(carList.getRecords().get(i).getCarSendOrderId());
            vo.setFlowInfoId(carList.getRecords().get(i).getFlowInfoId());
            vo.setFlowStage(carList.getRecords().get(i).getFlowStage());
            vo.setWorkOrderSubject(carList.getRecords().get(i).getCarSendOrderSubject());
            vo.setStatus(carList.getRecords().get(i).getStatus());
            vo.setWorkOrderContent(carList.getRecords().get(i).getCarSendOrderContent());
            vo.setWorkOrderCode(carList.getRecords().get(i).getCarSendOrderCode());
            vo.setCreateDate(carList.getRecords().get(i).getCreateDate());
            vo.setOrderType("3");
            vo.setOrderIcon("icon_car");
            list3.add(vo);
        }
        list.setRecords(list3);
        return list;
    }

    @RequestMapping(value = "/todo/{orderType}/{status}", method = RequestMethod.GET)
    public List<WorkOrderVo> listTodoByType(@PathVariable String status,@PathVariable String orderType,@RequestParam(required = false, defaultValue = "") String keyword, HttpServletRequest request) throws YiException {

		List<WorkOrderVo> res = new ArrayList<>();

    	if("car".equals(orderType)) {
        	List<CarSendOrderVo> res1 = carSendService.listTodo(status, keyword);
        	for(CarSendOrderVo vo : res1) {
        		res.add(map(vo));
        	}
    	}else if("market".equals(orderType)) {
    		List<MarketOrderVo> res3 = marketSendService.listTodo(status, keyword);
    		for(MarketOrderVo vo : res3) {
        		res.add(map(vo));
        	}
    	}else {
    		res = workOrderService.listTodo(status, keyword);
    	}
        return res;
    }
    
    
    private WorkOrderVo map(MarketOrderVo from) {
    	WorkOrderVo vo = new WorkOrderVo();
        vo.setWorkOrderId(from.getMarket_send_order_id());
        vo.setFlowInfoId(from.getFlowInfoId());
        vo.setFlowStage(from.getFlowStage());
        vo.setWorkOrderSubject(from.getWork_order_subject());
        vo.setStatus(from.getStatus());
        vo.setWorkOrderContent(from.getWo_order_content());
        vo.setWorkOrderCode(from.getMarket_send_order_code());
        vo.setCreateDate(from.getCreateDate());
        vo.setWatched(from.getWatched());
        vo.setOrderType("2");
        vo.setOrderIcon("icon_market");
        return vo;
    }
    
    private WorkOrderVo map(CarSendOrderVo from) {
    	WorkOrderVo vo = new WorkOrderVo();
        vo.setWorkOrderId(from.getCarSendOrderId());
        vo.setFlowInfoId(from.getFlowInfoId());
        vo.setFlowStage(from.getFlowStage());
        vo.setWorkOrderSubject(from.getCarSendOrderSubject());
        vo.setStatus(from.getStatus());
        vo.setWorkOrderContent(from.getCarSendOrderContent());
        vo.setWorkOrderCode(from.getCarSendOrderCode());
        vo.setCreateDate(from.getCreateDate());
        vo.setWatched(from.getWatched());
        vo.setOrderType("3");
        vo.setOrderIcon("icon_car");
        return vo;
    }
    
    @ApiOperation(value = "待办工单列表接口", notes = "此接口有一系列的查询,用status区分三个状态：处理中，已回单，已归档;返回参数包括：工单ID，工单名称，工单类型，派单人，派单时间等。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "工单状态", required = true, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页步长", required = false, dataType = "String")
    })
    @RequestMapping(value = "/todo/{status}", method = RequestMethod.GET)
    public Page<WorkOrderVo> listTodo(@PathVariable String status, @RequestParam(required = false, defaultValue = "1") int current,
                                      @RequestParam(required = false, defaultValue = "20") int size, @RequestParam(required = false, defaultValue = "") String keyword, HttpServletRequest request) throws YiException {

        String userId = CommonUtil.getUserIdFromSession(request);
        logger.debug("userid");
        Page<WorkOrderVo> list = workOrderService.listTodo(status, userId, current, size, keyword);
        Page<MarketOrderVo> list2 = marketSendService.listTodo(status,userId,current,size,keyword);
        Page<CarSendOrderVo> list3 = carSendService.listTodo(status,userId,current,size,keyword);

        //事务单
        for(WorkOrderVo workOrderVo : list.getRecords()){
        	workOrderVo.setOrderType("1");
        	workOrderVo.setOrderIcon("icon_thing");
        }
      //营销单
        for(MarketOrderVo marketOrderVo : list2.getRecords()){
        	list.getRecords().add(map(marketOrderVo));
        }
      //派車单
        for(CarSendOrderVo marketOrderVo : list3.getRecords()){
        	list.getRecords().add(map(marketOrderVo));
        }
        list.setTotal(list.getTotal() + list2.getTotal() + list3.getTotal());
        return list;
    }

    @ApiOperation(value = "待办工单列表接口", notes = "此接口有一系列的查询,用status区分三个状态：处理中，已回单，已归档;返回参数包括：工单ID，工单名称，工单类型，派单人，派单时间等。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "工单状态", required = true, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页步长", required = false, dataType = "String")
    })
    @RequestMapping(value = "/listOrderSave", method = RequestMethod.GET)
    public Page<WorkOrderVo> listOrderSave(@RequestParam(required = false, defaultValue = "1") int current,
                                           @RequestParam(required = false, defaultValue = "2000") int size, @RequestParam(required = false, defaultValue = "") String keyword, HttpServletRequest request) throws YiException {

        String userId = CommonUtil.getUserIdFromSession(request);

        Page<WorkOrderVo> list = workOrderService.listSave(userId, current, size, keyword);

        return list;
    }


    @ApiOperation(value = "我提交的工单", notes = "此接口有一系列的查询,用status区分三个状态：处理中，已回单，已归档;返回参数包括：工单ID，工单名称，工单类型，派单人，派单时间等。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "工单状态", required = true, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页步长", required = false, dataType = "String")
    })
    @RequestMapping(value = "/mine/{status}", method = RequestMethod.GET)
    public Page<WorkOrderVo> mySubmitOrderList(@PathVariable String status, @RequestParam(required = false, defaultValue = "1") int current,
                                               @RequestParam(required = false, defaultValue = "20") int size, @RequestParam(required = false, defaultValue = "") String keyword,
                                               HttpServletRequest request) throws YiException {

        String userId = CommonUtil.getUserIdFromSession(request);

        Page<WorkOrderVo> list = workOrderService.mySubmitOrderList(status, userId, current, size, keyword);
        Page<MarketOrderVo> list2 = marketSendService.mySubmitOrderList(status, userId, current, size, keyword);
        
        Page<CarSendOrderVo> carList = carSendService.mySubmitOrderList(status,userId,current,size,keyword);
        List<WorkOrderVo> list3 = new ArrayList<>();
        WorkOrderVo vo;
        //事务单
        for(int i = 0;i < list.getRecords().size();i ++){
            vo = new WorkOrderVo();
            vo.setWorkOrderId(list.getRecords().get(i).getWorkOrderId());
            vo.setFlowInfoId(list.getRecords().get(i).getFlowInfoId());
            vo.setFlowStage(list.getRecords().get(i).getWorkOrderSubject());
            vo.setWorkOrderSubject(list.getRecords().get(i).getWorkOrderSubject());
            vo.setStatus(list.getRecords().get(i).getStatus());
            vo.setWorkOrderContent(list.getRecords().get(i).getWorkOrderContent());
            vo.setWorkOrderCode(list.getRecords().get(i).getWorkOrderCode());
            vo.setCreateDate(list.getRecords().get(i).getCreateDate());
            vo.setWatched(list.getRecords().get(i).getWatched());
            vo.setOrderType("1");
            vo.setOrderIcon("icon_thing");
            list3.add(vo);
        }

        //营销单
        for(int i = 0;i < list2.getRecords().size();i++){
            vo = new WorkOrderVo();
            vo.setWorkOrderId(list2.getRecords().get(i).getMarket_send_order_id());
            vo.setFlowInfoId(list2.getRecords().get(i).getFlowInfoId());
            vo.setFlowStage(list2.getRecords().get(i).getFlowStage());
            vo.setWorkOrderSubject(list2.getRecords().get(i).getWork_order_subject());
            vo.setStatus(list2.getRecords().get(i).getStatus());
            vo.setWorkOrderContent(list2.getRecords().get(i).getWo_order_content());
            vo.setWorkOrderContent(list2.getRecords().get(i).getMarket_send_order_code());
            vo.setCreateDate(list2.getRecords().get(i).getCreateDate());
            vo.setWatched(list2.getRecords().get(i).getWatched());
            vo.setOrderType("2");
            vo.setOrderIcon("icon_market");
            list3.add(vo);
        }
        //派车单
        for(int i = 0;i < carList.getRecords().size();i++){
            vo = new WorkOrderVo();
            vo.setWorkOrderId(carList.getRecords().get(i).getCarSendOrderId());
            vo.setFlowInfoId(carList.getRecords().get(i).getFlowInfoId());
            vo.setFlowStage(carList.getRecords().get(i).getFlowStage());
            vo.setWorkOrderSubject(carList.getRecords().get(i).getCarSendOrderSubject());
            vo.setStatus(carList.getRecords().get(i).getStatus());
            vo.setWorkOrderContent(carList.getRecords().get(i).getCarSendOrderContent());
            vo.setWorkOrderCode(carList.getRecords().get(i).getCarSendOrderCode());
            vo.setCreateDate(carList.getRecords().get(i).getCreateDate());
            vo.setOrderType("3");
            vo.setOrderIcon("icon_car");
            list3.add(vo);
        }
        list.setRecords(list3);
        return list;
    }


    @ApiOperation(value = "收单接口", notes = "更新工单的阅读状态，已经阅读表示收单，别人不能再处理了")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderType", value = "工单类型，car/oa", required = true, dataType = "String"),
            @ApiImplicitParam(name = "orderId", value = "工单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "flowInfoId", value = "流程id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "tag", value = "标识位，为0，则表示重置", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/receiveorder/{orderType}/{orderId}/{flowInfoId}/{tag}", method = RequestMethod.POST)
    public String receiveOrder(@PathVariable String orderType, @PathVariable String orderId, @PathVariable String flowInfoId, @PathVariable String tag, HttpServletRequest request) throws YiException {

        String userId = CommonUtil.getUserIdFromSession(request);

        FlowInfoVo flowInfoVo = this.flowInfoMapper.selectById(flowInfoId);
        if (StringUtils.isNotEmpty(tag) && !tag.equalsIgnoreCase("0")) {
            flowInfoVo.setReceiveUserId(userId);
            flowInfoVo.setRecieveDate(new Date());
        } else {
            flowInfoVo.setReceiveUserId(null);

            flowInfoVo.setRecieveDate(null);
        }
        Integer tmp = this.flowInfoMapper.updateAllColumnById(flowInfoVo);

        boolean bool2 = false;
        String createUserId = null;

        String subject = null;
        if ("oa".equalsIgnoreCase(orderType)) {
            WorkOrderVo workOrderVo = this.workOrderService.selectById(orderId);
            workOrderVo.setEditable("0");

            bool2 = workOrderVo.updateById();

            createUserId = workOrderVo.getCreateById();
            subject = workOrderVo.getWorkOrderSubject();
        } else {
            CarSendOrderVo carSendOrderVo = this.carSendOrderMapper.selectById(orderId);
            carSendOrderVo.setEditable("0");

            bool2 = carSendOrderVo.updateById();
            createUserId = carSendOrderVo.getCreateById();
            subject = carSendOrderVo.getCarSendOrderSubject();
        }
        if (createUserId == null) {
            throw new YiException(SysErrorCnst.NO_CREATOR_ERROR, SysErrorCnst.MSG_NO_CREATOR_ERROR);
        }
        SysUser user = this.sysUserMapper.selectById(createUserId);
        StringBuffer sb = new StringBuffer("您主题为 ").append(subject).append(" 的工单已经被接单，进入处理阶段");

        eventBus.post(new SmsEvent(user.getMobile(), sb.toString()));
        return tmp > 0 && bool2 ? "1" : "0";
    }


    @ApiOperation(value = "工单详情", notes = "返回参数包括：工单ID，工单内容，工单名称，回单人，派单人，协办人，派单时间，回单时间，阶段回单时间，要求完成时间，要求阶段反馈时间，回单内容，阶段回单内容等。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workorderid", value = "工单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/detail/{workorderid}", method = RequestMethod.GET)
    public WorkOrderVo workorderDetail(@PathVariable String workorderid, HttpServletRequest request) throws YiException {

//        String userId = CommonUtil.getUserIdFromSession(request);

        WorkOrderVo workOrderVo = workOrderService.selectById(workorderid);
        
        String userId = CommonUtil.getUserIdFromSession(request);
        WatchOrderVo watchOrderVo = new WatchOrderVo();
        WatchOrderVo tmp = watchOrderVo.selectOne(new EntityWrapper<Object>().eq("order_id", workOrderVo.getWorkOrderId()).eq("user_id", userId).eq("delete_state", "0"));
        workOrderVo.setWatched(tmp == null ? null : "y");
        
        return workOrderVo;
    }
    

    @ApiOperation(value = "工单流程详情接口", notes = "展示流程信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workorderid", value = "工单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/flow/{workorderid}", method = RequestMethod.GET)
    public List<FlowInfoVo> flowInfo(@PathVariable String workorderid, HttpServletRequest request) {
        List<FlowInfoVo> list = flowInfoMapper.selectList(new EntityWrapper<FlowInfoVo>().eq("work_order_id", workorderid).orderBy("complete_date", false));
        return list;
    }


    @ApiOperation(value = "工单提交", notes = "工单提交，开始流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workOrderVo", value = "工单信息", required = true, dataType = "workOrderVo")
    })
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public String workorderSubmit(WorkOrderVo workOrderVo, HttpServletRequest request) throws YiException {
        String userId = CommonUtil.getUserIdFromSession(request);
        SysUser user = CommonUtil.getUserLoginInfoFromSession(request);
        return this.workOrderService.workorderSubmit(userId, user.getName(), workOrderVo);
    }

    @ApiOperation(value = "草稿保存", notes = "草稿保存，不发起流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workOrderVo", value = "工单信息", required = true, dataType = "workOrderVo")
    })
    @RequestMapping(value = "/saveOrder", method = RequestMethod.POST)
    public String saveOrder(WorkOrderVo workOrderVo, HttpServletRequest request) throws YiException {
        String userId = CommonUtil.getUserIdFromSession(request);
        SysUser user = CommonUtil.getUserLoginInfoFromSession(request);
        return this.workOrderService.workorderSave(userId, user.getName(), workOrderVo);
    }


    @ApiOperation(value = "通用回单接口", notes = "通用回单接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workOrderId", value = "工单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "event", value = "event,提交的动作,源于OaEvent", required = true, dataType = "String"),
            @ApiImplicitParam(name = "orderType", value = "工单类型，car/oa", required = true, dataType = "String")
    })
    @RequestMapping(value = "/flow/submit/{workOrderId}/{event}/{orderType}", method = RequestMethod.POST)
    public String submitFlow(@PathVariable String workOrderId, @PathVariable String event, @PathVariable String orderType, FlowInfoVo flowInfoVo, HttpServletRequest request) throws YiException {


        String userId = CommonUtil.getUserIdFromSession(request);

        SysUser user = CommonUtil.getUserLoginInfoFromSession(request);

        String curFlowStage = "";
        FlowHandlerVo flowHandlerVo = null;
        if (orderType.equalsIgnoreCase("oa")) {
            WorkOrderVo workOrderVo = this.workOrderService.selectById(workOrderId);
            curFlowStage = workOrderVo.getFlowStage();
            flowInfoVo.setFlowInfoId(workOrderVo.getFlowInfoId());
        } else if(orderType.equalsIgnoreCase("market")){
            MarketOrderVo marketOrderVo = this.marketOrderMapper.selectById(workOrderId);
            curFlowStage = marketOrderVo.getFlowStage();
            flowInfoVo.setFlowInfoId(marketOrderVo.getFlowInfoId());
        } else {
            CarSendOrderVo carSendOrderVo = carSendOrderMapper.selectById(workOrderId);
            curFlowStage = carSendOrderVo.getFlowStage();
            flowInfoVo.setFlowInfoId(carSendOrderVo.getFlowInfoId());
        }

        return this.flowService.flowSubmit(userId, user.getName(), workOrderId, curFlowStage, event, flowHandlerVo, flowInfoVo, orderType);

//        return this.workOrderService.flowSubmit(userId, userName, workOrderId, event, null, flowInfoVo);
    }

    /**
     * 批注的实现，就只直接忘t_flow_info里面插个记录
     *
     * @param workOrderId
     * @param flowInfoVo
     * @param request
     * @return
     * @throws YiException
     */
    @ApiOperation(value = "工单批注接口", notes = "工单批注接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workOrderId", value = "工单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/comment/{workOrderId}", method = RequestMethod.POST)
    public String comment(@PathVariable String workOrderId, FlowInfoVo flowInfoVo, HttpServletRequest request) throws YiException {


        String userId = CommonUtil.getUserIdFromSession(request);

        SysUser user = CommonUtil.getUserLoginInfoFromSession(request);

//        WorkOrderVo workOrderVo = this.workOrderService.selectById(workOrderId);

        return this.workOrderService.comment(userId, user.getName(), workOrderId, flowInfoVo);
    }

    /**
     * 签到的实现，就只直接忘t_flow_info里面插个记录
     *
     * @param workOrderId
     * @param flowInfoVo
     * @param request
     * @return
     * @throws YiException
     */
    @ApiOperation(value = "工单签到接口", notes = "工单签到接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workOrderId", value = "工单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/sign/{workOrderId}", method = RequestMethod.POST)
    public String sign(@PathVariable String workOrderId, FlowInfoVo flowInfoVo, HttpServletRequest request) throws YiException {


        String userId = CommonUtil.getUserIdFromSession(request);

        SysUser user = CommonUtil.getUserLoginInfoFromSession(request);
//        String address = request.getParameter("address");
//        String longitude = request.getParameter("longitude");
//        String lantitude = request.getParameter("lantitude");
//        WorkOrderVo workOrderVo = this.workOrderService.selectById(workOrderId);
//        this.workOrderService.saveSignInfo(workOrderId,address,longitude,lantitude);
        return this.workOrderService.sign(userId, user.getName(), workOrderId, flowInfoVo);
    }

    /*******************************************************************************************************************************************************************************
     * 下面一长串接口没啥用处，一开始没想好
     *
     *
     */

    @ApiOperation(value = "回单接口", notes = "回单接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workOrderId", value = "工单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "event", value = "event,提交的动作,源于OaEvent", required = true, dataType = "String")
    })
    @RequestMapping(value = "/process/finish/{workOrderId}/{event}", method = RequestMethod.POST)
    public String completeOrder(@PathVariable String workOrderId, @PathVariable String event, FlowInfoVo flowInfoVo, HttpServletRequest request) throws YiException {
        return this.submitFlow(workOrderId, event, "oa", flowInfoVo, request);
    }


    @ApiOperation(value = "阶段回单接口", notes = "阶段回单接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workOrderId", value = "工单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "event", value = "event,提交的动作,源于OaEvent", required = true, dataType = "String")
    })
    @RequestMapping(value = "/process/milestone/{workOrderId}", method = RequestMethod.POST)
    public String milestone(@PathVariable String workOrderId, @PathVariable String event, FlowInfoVo flowInfoVo, HttpServletRequest request) throws YiException {

        return this.submitFlow(workOrderId, event, "oa", flowInfoVo, request);
    }


    /**
     * 回单审批接口
     *
     * @param workOrderId
     * @param flowInfoVo
     * @param request
     * @return
     * @throws YiException
     */
    @ApiOperation(value = "回单审批接口", notes = "回单审批接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workOrderId", value = "工单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "event", value = "event,提交的动作,源于OaEvent", required = true, dataType = "String")
    })
    @RequestMapping(value = "/approve/submit/{workOrderId}/{event}", method = RequestMethod.POST)
    public String approve(@PathVariable String workOrderId, @PathVariable String event, FlowInfoVo flowInfoVo, HttpServletRequest request) throws YiException {

        return this.submitFlow(workOrderId, event, "oa", flowInfoVo, request);
    }


    @Deprecated
    @ApiOperation(value = "支持的操作接口", notes = "本操作暂时还未实现，需要了解sm的API才行")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workOrderId", value = "工单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "event", value = "event,提交的动作,源于OaEvent", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getoperation/{workOrderId}", method = RequestMethod.GET)
    public List<String> getOperation(@PathVariable String workOrderId, HttpServletRequest request) throws YiException {

        return null;
    }


    @ApiOperation(value = "查询某订单的指定环节信息", notes = "回单审批接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workOrderId", value = "工单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "event", value = "event,提交的动作,源于OaEvent", required = true, dataType = "String")
    })
    @RequestMapping(value = "/flowinfo/{workOrderId}/{flowstage}", method = RequestMethod.GET)
    public List<FlowInfoVo> getFlowInfoVoByOrderIdAndStage(@PathVariable String workOrderId, @PathVariable String flowstage, HttpServletRequest request) throws YiException {
        List<FlowInfoVo> list = this.flowInfoMapper.selectList(new EntityWrapper<FlowInfoVo>().eq("work_order_id", workOrderId).eq("flow_stage", flowstage).orderBy("create_date", false));
        return list;
    }


    @ApiOperation(value = "是否具备制定订单的流程处理权限", notes = "是否具备制定订单的流程处理权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workOrderId", value = "工单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/candeal/{workOrderId}", method = RequestMethod.GET)
    public String canDeal(@PathVariable String workOrderId, HttpServletRequest request) throws YiException {
        String userId = CommonUtil.getUserIdFromSession(request);

        boolean bool = this.workOrderService.canDeal(workOrderId, userId);
        return bool ? "1" : "-1";
    }


    @ApiOperation(value = "工单提交", notes = "工单提交，开始流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workOrderVo", value = "工单信息", required = true, dataType = "workOrderVo")
    })
    @RequestMapping(value = "/addpanel", method = RequestMethod.POST)
    public String addpanel(WorkOrderVo workOrderVo, HttpServletRequest request) throws YiException {
        String userId = CommonUtil.getUserIdFromSession(request);
        SysUser user = CommonUtil.getUserLoginInfoFromSession(request);
        return this.workOrderService.workorderSubmit(userId, user.getName(), workOrderVo);
    }

}
