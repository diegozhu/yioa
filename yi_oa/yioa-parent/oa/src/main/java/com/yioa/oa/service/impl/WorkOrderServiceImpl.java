package com.yioa.oa.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yioa.common.exception.YiException;
import com.yioa.common.util.CommonUtil;
import com.yioa.common.util.PageUtil;
import com.yioa.common.util.SysErrorCnst;
import com.yioa.oa.domain.WorkOrderVo;
import com.yioa.oa.mapper.WorkOrderMapper;
import com.yioa.oa.service.IWorkOrderService;
import com.yioa.ssm.domain.FlowCustParam;
import com.yioa.ssm.domain.FlowHandlerVo;
import com.yioa.ssm.domain.FlowInfoVo;
import com.yioa.ssm.service.FlowService;
import com.yioa.ssm.service.HandlerService;
import com.yioa.ssm.util.FlowUtil;
import com.yioa.ssm.util.OaEvents;
import com.yioa.ssm.util.OaFlows;
import com.yioa.sys.domain.SysRole;
import com.yioa.sys.domain.SysUser;
import com.yioa.sys.mapper.SysRoleMapper;
import com.yioa.sys.mapper.SysUserMapper;
import com.yioa.sys.service.SysUserService;

/**
 * Created by tao on 2017-05-23.
 */
@Service
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrderVo> implements IWorkOrderService {

    private static final Logger logger = LoggerFactory.getLogger(WorkOrderServiceImpl.class);

    @Autowired
    private WorkOrderMapper workOrderMapper;


    @Autowired
    private FlowService flowService;

    @Autowired
    private HandlerService handlerService;


    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserService sysUserService;


    @Autowired
    private SysRoleMapper sysRoleMapper;


//    @Override
//    public Page<WorkOrderVo> listTodo(String status, String userId, int current, int size) {
//
//        //获得工单
//        Page<WorkOrderVo> workOrderPage = this.selectPage(new Page<WorkOrderVo>(current, size), new EntityWrapper<WorkOrderVo>().eq("status", status).eq("create_by_id", userId));
//
//        //因为mybatis plus 不支持join，所以才使用了如此费解的方式
//        if(!workOrderPage.getRecords().isEmpty()){
//            List<String> list = Lists.transform(workOrderPage.getRecords(), new Function<WorkOrderVo, String>() {
//                @Override
//                public String apply(WorkOrderVo workOrder) {
//                    return workOrder.getWorkOrderId();
//                }
//            });
//        }
//        return workOrderPage;
//    }


    @Override
    public Page<WorkOrderVo> listTodo(String status, String userId, int current, int size, String keyword) {

        int offset = PageUtil.getOffset(current, size);

        List<String> idList = this.getRoleIdStrByUserId(userId);

        Map<String, Object> map = Maps.newHashMap();
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


//        Map<String, Object> map = ImmutableMap.of("likeUserId", "%," + userId + ",%", column, userId, "offset", offset, "size", size,"roleIdStr", "," + StringUtils.join(idList, ",") + ",");;

        List<WorkOrderVo> list = this.workOrderMapper.selectWorkOrderByStatusAndUserId(map);
        int total = this.workOrderMapper.selectWorkOrderCountByStatusAndUserId(map);

        Page<WorkOrderVo> page = new Page<>();
        page.setRecords(list);
        page.setCurrent(current);
        page.setSize(size);
        page.setCondition(map);
        page.setTotal(total);

        return page;
    }
    
    @Override
    public List<WorkOrderVo> listTodo(String status, String keyword) {

        Map<String, Object> map = Maps.newHashMap();
        map.put("status", status);

        //搜索条件
        if (StringUtils.isNotEmpty(keyword)) {
            map.put("keyword", "%" + keyword + "%");
        }

        List<WorkOrderVo> list = this.workOrderMapper.selectWorkOrderByStatus(map);
        return list;
    }

    @Override
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

    private List<String> getRoleIdStrByUserId(String userId) {
        Map<String, Object> map = ImmutableMap.of("userId", userId);
        List<SysRole> reList = this.sysUserMapper.queryRoleByUserId(map);
        List<String> idList = reList.stream().map(SysRole::getId).collect(Collectors.toList());
        return idList;
    }


    @Override
    public Page<WorkOrderVo> mySubmitOrderList(String status, String userId, int current, int size, String keyword) {

        String column = "create_by_id";
        int offset = PageUtil.getOffset(current, size);
        Map<String, Object> map = null;

        if (StringUtils.isNotEmpty(keyword)) {
            map = ImmutableMap.of("status", status, column, userId, "offset", offset, "size", size, "keyword", "%" + keyword + "%");
        } else {
            map = ImmutableMap.of("status", status, column, userId, "offset", offset, "size", size);
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
     * 工单批注
     * 本方法是通用的，OA 和 car都适合
     *
     * @param userId
     * @param userName
     * @param workOrderId
     * @param flowInfoVo
     * @return
     * @throws YiException
     */
    @Override
    public String comment(String userId, String userName, String workOrderId, FlowInfoVo flowInfoVo) throws YiException {

        FlowInfoVo tFlowInfoVo = new FlowInfoVo();
        tFlowInfoVo.setNotes(flowInfoVo.getNotes());
        tFlowInfoVo.setWorkOrderId(workOrderId);
        tFlowInfoVo.setHandleByType(FlowUtil.USER_HANDLER_TYPE);
        tFlowInfoVo.setHandleBy(userName);
        tFlowInfoVo.setHandleById(userId);
        tFlowInfoVo.setFlowStageName(FlowUtil.COMMENT_FLOW_STAGE_NAME);
        tFlowInfoVo.setFlowStage(FlowUtil.COMMENT_FLOW_STAGE);

        tFlowInfoVo.setAttachFile(flowInfoVo.getAttachFile());
        tFlowInfoVo.setFlowInfoId(CommonUtil.getUUID());
        tFlowInfoVo.setCreateDate(new Date());

        tFlowInfoVo.setCompleteDate(new Date());
        tFlowInfoVo.setCompleteUserId(userId);
        tFlowInfoVo.setCompleteUserName(userName);

        tFlowInfoVo.insert();


        return "1";
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
    @Override
    public String sign(String userId, String userName, String workOrderId, FlowInfoVo flowInfoVo) throws YiException {

        FlowInfoVo tFlowInfoVo = new FlowInfoVo();
        tFlowInfoVo.setNotes(flowInfoVo.getNotes());
        tFlowInfoVo.setWorkOrderId(workOrderId);
        tFlowInfoVo.setHandleByType(FlowUtil.USER_HANDLER_TYPE);
        tFlowInfoVo.setHandleBy(userName);
        tFlowInfoVo.setHandleById(userId);
        tFlowInfoVo.setFlowStageName(FlowUtil.SIGN_FLOW_STAGE_NAME);
        tFlowInfoVo.setFlowStage(FlowUtil.SIGN_FLOW_STAGE);

        tFlowInfoVo.setAttachFile(flowInfoVo.getAttachFile());
        tFlowInfoVo.setFlowInfoId(CommonUtil.getUUID());
        tFlowInfoVo.setCreateDate(new Date());

        tFlowInfoVo.setCompleteDate(new Date());
        tFlowInfoVo.setCompleteUserId(userId);
        tFlowInfoVo.setCompleteUserName(userName);
        tFlowInfoVo.setAddress(flowInfoVo.getAddress());
        tFlowInfoVo.setLongitude(flowInfoVo.getLongitude());
        tFlowInfoVo.setLatitude(flowInfoVo.getLatitude());
        tFlowInfoVo.insert();


        return "1";
    }

    @Override
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

    private String getIdsByLoginNameStr(String loginNameStr) {

        String[] arr = StringUtils.split(loginNameStr, ",,;");


        List<SysUser> list = this.sysUserMapper.selectList(new EntityWrapper<SysUser>().in("login_name", arr));

        List<String> relist = Lists.transform(list, new Function<SysUser, String>() {
            @Override
            public String apply(SysUser user) {
                return user.getId();
            }
        });

        String str = StringUtils.join(relist, ",");

        return str;
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
    @Override
    public String workorderSubmit(String userId, String userName, WorkOrderVo workOrderVo) throws YiException {

        if (StringUtils.isEmpty(workOrderVo.getWorkOrderId())) {
            String tmp = this.workorderSave(userId, userName, workOrderVo);
            if (!tmp.equalsIgnoreCase("1")) {
                throw new RuntimeException("存储工单错误");
            }
        }else{
            workOrderVo.setCreateDate(new Date());
            workOrderVo.setStatus(OaFlows.FlOW_START.getName());
            boolean bool = this.updateById(workOrderVo);
            if (!bool) {
                throw new RuntimeException("存储工单错误");
            }
        }

//        List<SysUser> userList = sysUserService.getUserById(ImmutableList.copyOf(StringUtils.split(workOrderVo.getOrganiser(), ",")));
//
//        List<String> userNameList = Lists.transform(userList, new Function<SysUser, String>() {
//            @Override
//            public String apply(SysUser user) {
//                return user.getLoginName();
//            }
//        });
        FlowHandlerVo flowHandlerVo = new FlowHandlerVo();
        flowHandlerVo.setHandlerById(workOrderVo.getOrganiser());
        flowHandlerVo.setHandlerBy(workOrderVo.getOrganiserName());
        flowHandlerVo.setHandlerByType(FlowUtil.USER_HANDLER_TYPE);
        //启动流程
        FlowCustParam flowCustParam = new FlowCustParam(workOrderVo.getWorkOrderId(), userName, userId, null, flowHandlerVo, FlowUtil.ORDER_OA);

        boolean suc = flowService.subFlow(flowCustParam, OaFlows.FlOW_START.name(), OaEvents.SUBMIT.name());

        if (!suc) {
            throw new YiException(SysErrorCnst.FLOW_RUN_ERR, SysErrorCnst.MSG_FLOW_RUN_ERR);
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
    @Override
    public String workorderSave(String userId, String userName, WorkOrderVo workOrderVo) throws YiException {
        String currentFlowStage = OaFlows.FlOW_START.name();
        //存储workorder
        if (StringUtils.isEmpty(workOrderVo.getWorkOrderId())) {
            workOrderVo.setWorkOrderId(CommonUtil.getUUID());
        }
        //工单编码的规则，前缀O+日期+序号
        workOrderVo.setWorkOrderCode(CommonUtil.genOrderCode("O"));

        workOrderVo.setFlowStage(currentFlowStage);
        workOrderVo.setFlowStageName(OaFlows.valueOf(currentFlowStage).getName());

        //存储实际的名字而不是登录名
        workOrderVo.setCreateBy(userName);
        workOrderVo.setCreateById(userId);
        workOrderVo.setCreateDate(new Date());
        workOrderVo.setStatus("草稿");

        boolean bool = this.insert(workOrderVo);
        if (!bool) {
            throw new RuntimeException("存储工单错误");
        }
        return "1";
    }


    @Override
    public String flowSubmit(String userId, String userName, String workOrderId, String event, FlowHandlerVo flowHandlerVo, FlowInfoVo flowInfoVo) throws YiException {

        //先走流程，再改工单状态
        FlowCustParam flowCustParam = new FlowCustParam(workOrderId,
                userName,
                userId,
                flowInfoVo.getFlowInfoId(), flowHandlerVo, FlowUtil.ORDER_OA);
        flowCustParam.setNotes(flowInfoVo.getNotes());
        flowCustParam.setAttachFile(flowInfoVo.getAttachFile());
        flowCustParam.setPreFlowInfoId(flowInfoVo.getFlowInfoId());

        WorkOrderVo workOrderVo = this.workOrderMapper.selectById(workOrderId);

        boolean suc = flowService.subFlow(flowCustParam, workOrderVo.getFlowStage(), event);

        if (!suc) {
            throw new YiException(SysErrorCnst.FLOW_RUN_ERR, SysErrorCnst.MSG_FLOW_RUN_ERR);
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
    @Override
    public boolean canDeal(String workOrderId, String userId) {


        List<String> idList = this.getRoleIdStrByUserId(userId);

        Map<String, Object> map = Maps.newHashMap();
//        map.put("likeUserId", "%," + userId + ",%");
        map.put("workOrderId", workOrderId);
        map.put("userId", userId);
        map.put("roleIdStr", "," + StringUtils.join(idList, ",") + ",");
        int total = this.workOrderMapper.selectCanDealCount(map);
        return total > 0;

    }


}
