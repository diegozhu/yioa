package com.yioa.car.controller;

import java.util.List;
import java.util.Map;

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
import com.google.common.collect.Maps;
import com.yioa.car.domain.CarSendOrderVo;
import com.yioa.car.domain.CarVo;
import com.yioa.car.mapper.CarMapper;
import com.yioa.car.mapper.CarSendOrderMapper;
import com.yioa.car.service.CarSendService;
import com.yioa.common.exception.YiException;
import com.yioa.common.util.CommonUtil;
import com.yioa.common.util.PageUtil;
import com.yioa.ssm.domain.FlowInfoVo;
import com.yioa.ssm.service.FlowService;
import com.yioa.sys.domain.SysUser;
import com.yioa.sys.mapper.SysUserMapper;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Created by tao on 2017-06-04.
 */
@RestController
@RequestMapping("/car")
public class CarController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private SysUserMapper sysUserMapper;


    @Autowired
    private CarMapper carMapper;

    @Autowired
    private CarSendService carSendService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private CarSendOrderMapper carSendOrderMapper;


    @ApiOperation(value = "驾驶员查询接口", notes = "驾驶员查询接口,查询所有驾驶员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "exStr", value = "已选人员，需要排除", required = false, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页步长", required = true, dataType = "String")
    })


    @RequestMapping(value = "/driver/list/{exStr}", method = RequestMethod.GET)
    public Page<SysUser> listDriver(@PathVariable(required = false, value = "") String exStr, @RequestParam int current, @RequestParam int size, HttpServletRequest request) {

//        int offset = PageUtil.getOffset(current, size);
//
//        String[] exIdArr = StringUtils.split(exStr,",");
//
//        SysUser driverVo = new SysUser();
//        Page<SysUser> page = driverVo.selectPage(new Page<SysUser>(current, size), new EntityWrapper<SysUser>().eq("del_flag", "0").eq("driver_flag", "1").notIn("id", Arrays.asList(exIdArr)));
//
//        LOGGER.info("#### listDriver return {}", page);
//        return page;


        return this.commonStaffQry(exStr, current, size, true);
    }


    @ApiOperation(value = "驾驶员查询接口-过滤", notes = "查询所有驾驶员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "exStr", value = "已选人员，需要排除", required = false, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "搜索关键字", required = true, dataType = "String"),
            @ApiImplicitParam(name = "maxNum", value = "最大返回跳数，不超过100", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/driver/q/{keyword}/exStr/{maxNum}", method = RequestMethod.GET)
    public List<SysUser> listDriverBykey(@PathVariable(required = false, value = "") String exStr, @PathVariable String keyword, @PathVariable int maxNum, HttpServletRequest request) {
        maxNum = maxNum > 100 ? 100 : maxNum;

//        Map<String, Object> map = ImmutableMap.of("maxNum", Integer.valueOf(maxNum), "login_name", keyword, "name", keyword, "phone", keyword, "mobile", keyword);

        Map<String, Object> tMap = Maps.newHashMap();
        tMap.put("maxNum", Integer.valueOf(maxNum));
        tMap.put("login_name", keyword);
        tMap.put("name", keyword);
        tMap.put("phone", keyword);
        tMap.put("mobile", keyword);

        //司机的过滤条件
        tMap.put("driver_flag", "1");
        CommonUtil.setExIdStr(exStr, tMap);
        List<SysUser> list = this.sysUserMapper.querySysUserByKeyword(tMap);
        return list;
    }


    private Page<SysUser> commonStaffQry(String exStr, int current, int size, boolean qryDriver) {
        int offset = PageUtil.getOffset(current, size);
        Map<String, Object> tMap = Maps.newHashMap();

        CommonUtil.setExIdStr(exStr, tMap);
        if (qryDriver) {
            tMap.put("driver_flag", "1");
        }
        Page<SysUser> page = new Page<>();
        List<SysUser> list = this.sysUserMapper.querySysUserByKeyword(tMap);

        int total = this.sysUserMapper.querySysUserCntByKeyword(tMap);
        page.setRecords(list);
        page.setCondition(tMap);
        page.setCurrent(current);
        page.setSize(size);
        page.setTotal(total);
        LOGGER.info("#### staffList return {}", page);
        return page;


    }


    @ApiOperation(value = "乘坐人查询接口", notes = "乘坐人查询接口,查询所有乘客")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "exStr", value = "已选人员，需要排除", required = false, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页步长", required = true, dataType = "String")
    })
    @RequestMapping(value = "/passenger/list/{exStr}", method = RequestMethod.GET)
    public Page<SysUser> listPassenger(@PathVariable(required = false, value = "") String exStr, @RequestParam int current, @RequestParam int size, HttpServletRequest request) {
        return this.commonStaffQry(exStr, current, size, false);

    }


    @ApiOperation(value = "乘坐人查询接口-过滤", notes = "查询所有乘坐人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "exStr", value = "已选人员，需要排除", required = false, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "搜索关键字", required = true, dataType = "String"),
            @ApiImplicitParam(name = "maxNum", value = "最大返回跳数，不超过100", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/passenger/q/{keyword}/{exStr}/{maxNum}", method = RequestMethod.GET)
    public List<SysUser> listPassengerBykey(@PathVariable(required = false, value = "") String exStr, @PathVariable String keyword, @PathVariable int maxNum, HttpServletRequest request) {
        maxNum = maxNum > 100 ? 100 : maxNum;

//        Map<String, Object> map = ImmutableMap.of("maxNum", Integer.valueOf(maxNum), "login_name", keyword, "name", keyword, "phone", keyword, "mobile", keyword);

        Map<String, Object> tMap = Maps.newHashMap();
        tMap.put("maxNum", Integer.valueOf(maxNum));
        tMap.put("login_name", keyword);
        tMap.put("name", keyword);
        tMap.put("phone", keyword);
        tMap.put("mobile", keyword);
        CommonUtil.setExIdStr(exStr, tMap);

        List<SysUser> list = this.sysUserMapper.querySysUserByKeyword(tMap);
        return list;
    }


    @ApiOperation(value = "汽车查询接口", notes = "驾驶员查询接口,查询所有驾驶员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页步长", required = true, dataType = "String")
    })
    @RequestMapping(value = "/car/list", method = RequestMethod.GET)
    public Page<CarVo> listCar(@RequestParam int current, @RequestParam int size, HttpServletRequest request) {

        int offset = PageUtil.getOffset(current, size);

        CarVo carVo = new CarVo();

        Page<CarVo> page = new Page<>();

        Map<String, Object> map = Maps.newHashMap();
        List<CarVo> carList = this.carSendOrderMapper.selectCar(map);

        page.setTotal(carList.size());
        page.setRecords(carList);
        page.setCurrent(current);
        page.setSize(size);
        page.setCondition(map);

//        carList.stream().forEach();
//        Page<CarVo> page = carVo.selectPage(new Page<CarVo>(current, size), new EntityWrapper<CarVo>().eq("delete_tag", "0"));

        LOGGER.info("#### listDriver return {}", page);
        return page;
    }


    @ApiOperation(value = "汽车查询接口-过滤", notes = "查询所有汽车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "搜索关键字", required = true, dataType = "String")
    })
    @RequestMapping(value = "/car/q/{keyword}", method = RequestMethod.GET)
    public List<CarVo> carListBykey(@PathVariable String keyword, @PathVariable int maxNum, HttpServletRequest request) {
        maxNum = maxNum > 100 ? 100 : maxNum;
        //FIXME 这个查询，需要写个用例验证一下，还没试过
        CarVo carVo = new CarVo();
//        List<CarVo> list = carVo.selectList(new EntityWrapper<CarVo>().like("car_no", keyword).like("car_type", keyword).eq("delete_tag", "0"));
        List<CarVo> list = carVo.selectList(new EntityWrapper<CarVo>().eq("delete_tag", "0").like("car_no", keyword).like("car_type", keyword));
        return list;
    }


    @ApiOperation(value = "派车提交", notes = "派车工单提交，开始流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carSendOrderVo", value = "工单信息", required = true, dataType = "CarSendOrderVo")
    })
    @RequestMapping(value = "/order/submit", method = RequestMethod.POST)
    public String orderSubmit(CarSendOrderVo carSendOrderVo, HttpServletRequest request) throws YiException {
        String userId = CommonUtil.getUserIdFromSession(request);
        SysUser user = CommonUtil.getUserLoginInfoFromSession(request);
        return this.carSendService.orderSubmit(userId, user.getName(), carSendOrderVo);
    }


    @ApiOperation(value = "保存草稿", notes = "派车工单保存草稿")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carSendOrderVo", value = "工单信息", required = true, dataType = "CarSendOrderVo")
    })
    @RequestMapping(value = "/order/saveOrder", method = RequestMethod.POST)
    public String saveOrder(CarSendOrderVo carSendOrderVo, HttpServletRequest request) throws YiException {


        String userId = CommonUtil.getUserIdFromSession(request);

        SysUser user = CommonUtil.getUserLoginInfoFromSession(request);

        return this.carSendService.saveOrder(userId, user.getName(), carSendOrderVo);
    }



    @ApiOperation(value = "通用回单接口", notes = "通用回单接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carSendOrderId", value = "工单id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "event", value = "event,提交的动作,源于OaEvent", required = true, dataType = "String"),
            @ApiImplicitParam(name = "orderType", value = "orderType,oa", required = true, dataType = "String")
    })
    @RequestMapping(value = "/flow/submit/{carSendOrderId}/{event}/{orderType}", method = RequestMethod.POST)
    public String submitFlow(@PathVariable String carSendOrderId, @PathVariable String event, @PathVariable String orderType, FlowInfoVo flowInfoVo, HttpServletRequest request) throws YiException {


        String userId = CommonUtil.getUserIdFromSession(request);
        SysUser user = CommonUtil.getUserLoginInfoFromSession(request);

        CarSendOrderVo carSendOrderVo = this.carSendOrderMapper.selectById(carSendOrderId);

        return this.flowService.flowSubmit(userId, user.getName(), carSendOrderId, carSendOrderVo.getFlowStage(), event, null, flowInfoVo, orderType);

//        return this.workOrderService.flowSubmit(userId, userName, workOrderId, event, null, flowInfoVo);
    }


    @ApiOperation(value = "待办工单列表接口", notes = "此接口有一系列的查询,用status区分三个状态：处理中，已回单，已归档;返回参数包括：工单ID，工单名称，工单类型，派单人，派单时间等。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "工单状态", required = true, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页步长", required = true, dataType = "String")
    })
    @RequestMapping(value = "/order/todo/{status}", method = RequestMethod.GET)
    public Page<CarSendOrderVo> listTodo(@PathVariable String status, @RequestParam int current, @RequestParam int size,
                                         @RequestParam(required = false, defaultValue = "") String keyword, HttpServletRequest request) throws YiException {

        String userId = CommonUtil.getUserIdFromSession(request);

        Page<CarSendOrderVo> list = this.carSendService.listTodo(status, userId, current, size, keyword);

        return list;
    }


    @ApiOperation(value = "派车单草稿", notes = "派车单的草稿")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页步长", required = true, dataType = "String")
    })
    @RequestMapping(value = "/order/listCarOrderSave", method = RequestMethod.GET)
    public Page<CarSendOrderVo> listCarOrderSave(@RequestParam(required = false, defaultValue = "1") int current,
                                                 @RequestParam(required = false, defaultValue = "2000") int size,
                                         @RequestParam(required = false, defaultValue = "") String keyword, HttpServletRequest request) throws YiException {

        String userId = CommonUtil.getUserIdFromSession(request);
        Page<CarSendOrderVo> page = this.carSendService.selectPage(new Page<>(current,size),new EntityWrapper<CarSendOrderVo>().eq("status", "草稿").eq("create_by_id", userId));
        return page;
    }



    @ApiOperation(value = "我提交的工单", notes = "此接口有一系列的查询,用status区分三个状态：处理中，已回单，已归档;返回参数包括：工单ID，工单名称，工单类型，派单人，派单时间等。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "工单状态", required = true, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页步长", required = true, dataType = "String")
    })
    @RequestMapping(value = "/order/mine/{status}", method = RequestMethod.GET)
    public Page<CarSendOrderVo> mySubmitOrderList(@PathVariable String status, @RequestParam int current, @RequestParam int size,
                                                  @RequestParam(required = false, defaultValue = "") String keyword, HttpServletRequest request) throws YiException {

        String userId = CommonUtil.getUserIdFromSession(request);

        Page<CarSendOrderVo> page = this.carSendService.mySubmitOrderList(status, userId, current, size, keyword);

        return page;
    }


    @ApiOperation(value = "派车工单详情", notes = "返回所有提交的菜蔬。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carSendOrderId", value = "工单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/detail/{carSendOrderId}", method = RequestMethod.GET)
    public CarSendOrderVo workorderDetail(@PathVariable String carSendOrderId, HttpServletRequest request) throws YiException {

        CarSendOrderVo carSendOrderVo = this.carSendOrderMapper.selectById(carSendOrderId);
        SysUser driver = sysUserMapper.selectById(carSendOrderVo.getDriverId());
        carSendOrderVo.setDriverPhone(driver == null ? "" : StringUtils.isBlank(driver.getMobile()) ? driver.getMobile() : driver.getMobile());
        return carSendOrderVo;
    }


    @ApiOperation(value = "是否具备制定订单的流程处理权限", notes = "是否具备制定订单的流程处理权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "工单id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/workorder/candeal/{orderId}", method = RequestMethod.GET)
    public String canDeal(@PathVariable String orderId, HttpServletRequest request) throws YiException {
        String userId = CommonUtil.getUserIdFromSession(request);

        boolean bool = this.carSendService.canDeal(orderId, userId);
        return bool ? "1" : "-1";
    }

}
