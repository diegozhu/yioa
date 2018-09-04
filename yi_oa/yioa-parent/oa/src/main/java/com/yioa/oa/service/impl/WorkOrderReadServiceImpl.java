package com.yioa.oa.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yioa.oa.domain.WorkOrderReadVo;
import com.yioa.oa.service.IWorkOrderReadService;
import com.yioa.oa.mapper.WorkOrderReadMapper;
import com.yioa.oa.util.OaCnst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tao on 2017-05-23.
 */
@Deprecated
@Service
public class WorkOrderReadServiceImpl extends ServiceImpl<WorkOrderReadMapper, WorkOrderReadVo> implements IWorkOrderReadService {


    @Autowired
    private WorkOrderReadMapper workOrderReadMapper;

    @Override
    public List<WorkOrderReadVo> getWorkOrderRead(List<String> workOrderList, String userId) {

        List<WorkOrderReadVo> readVoList = workOrderReadMapper.selectList(new EntityWrapper<WorkOrderReadVo>().in("work_order_id", workOrderList).eq("read_user_id", userId));
        return readVoList;
    }

    @Override
    public boolean readWorkOrder(String workOrderId, String readStatus, String userId) {

        WorkOrderReadVo workOrderReadVo = new WorkOrderReadVo(workOrderId, userId);
        WorkOrderReadVo tmp = workOrderReadVo.selectOne(new EntityWrapper().eq("work_order_id", workOrderId).eq("read_user_id", userId));

        if (tmp != null) {
            tmp.setReadStaus(readStatus);
            workOrderReadVo.updateById();
            return true;
        } else {
            workOrderReadVo.setReadStaus(readStatus);
            return workOrderReadVo.insert();
        }
    }

    @Override
    public boolean readWorkOrder(String workOrderId, String userId) {
        return this.readWorkOrder(workOrderId, OaCnst.WORK_ORDER_STATUS_TODO, userId);
    }

}
