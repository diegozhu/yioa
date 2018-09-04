package com.yioa.oa.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.yioa.common.exception.YiException;
import com.yioa.oa.domain.WorkOrderVo;
import com.yioa.ssm.domain.FlowHandlerVo;
import com.yioa.ssm.domain.FlowInfoVo;

/**
 * Created by tao on 2017-05-23.
 */
public interface IWorkOrderService extends IService<WorkOrderVo> {

    public Page<WorkOrderVo> listTodo(String status, String userId, int current, int size,String keyword);

    public Page<WorkOrderVo> listSave(String userId, int current, int size,String keyword);

    public Page<WorkOrderVo> mySubmitOrderList(String status, String userId, int current, int size,String keyword);



    /**
     * 启动流程
     * @param userId
      * @param workOrderVo
     * @return
     * @throws YiException
     */
    public String workorderSubmit(String userId, String userName, WorkOrderVo workOrderVo) throws YiException;


    /**
     * 保存草稿
     * @param userId
     * @param userName
     * @param workOrderVo
     * @return
     * @throws YiException
     */
    public String workorderSave(String userId, String userName, WorkOrderVo workOrderVo) throws YiException;




    /**
     * 每个环节的提交动作
     * @param userId
     * @param userName
     * @param workOrderId
     * @param event
     * @param flowHandlerVo
     * @param flowInfoVo
     * @return
     * @throws YiException
     */
    public String flowSubmit(String userId, String userName, String workOrderId, String event, FlowHandlerVo flowHandlerVo, FlowInfoVo flowInfoVo) throws YiException;

    public String comment(String userId, String userName, String workOrderId, FlowInfoVo flowInfoVo) throws YiException;
    public String sign(String userId, String userName, String workOrderId, FlowInfoVo flowInfoVo) throws YiException;
    public String saveSignInfo(String workOrderId,String address,String longitude,String lantitude) throws  YiException;
    public boolean canDeal(String workOrderId, String userId);

	List<WorkOrderVo> listTodo(String status, String keyword);

//    public String completeOrder(String userId, String userName, String workOrderId, FlowInfoVo flowInfoVo) throws YiException;
//
//    public String milestone(String userId, String userName, String workOrderId, FlowInfoVo flowInfoVo) throws YiException;
//
//

//
//    public String approve(String userId, String userName, String workOrderId, String pass,FlowInfoVo flowInfoVo) throws YiException;


}
