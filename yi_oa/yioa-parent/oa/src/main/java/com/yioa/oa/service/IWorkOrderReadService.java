package com.yioa.oa.service;

import com.baomidou.mybatisplus.service.IService;
import com.yioa.oa.domain.WorkOrderReadVo;

import java.util.List;

/**
 * Created by tao on 2017-05-23.
 */
@Deprecated
public interface IWorkOrderReadService extends IService<WorkOrderReadVo> {

    public List<WorkOrderReadVo> getWorkOrderRead(List<String> workOrderList, String userId);


    public boolean readWorkOrder(String workOrderId, String userId);


    public boolean readWorkOrder(String workOrderId, String readStatus ,String userId) ;

}
