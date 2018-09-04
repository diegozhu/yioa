package com.yioa.oa.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yioa.oa.domain.WorkOrderVo;

/**
 * Created by tao on 2017-05-23.
 */
public interface WorkOrderMapper extends BaseMapper<WorkOrderVo> {


    public List<WorkOrderVo> selectWorkOrderByStatusAndUserId(Map<String, Object> map);
    public List<WorkOrderVo> selectWorkOrderByStatus(Map<String, Object> map);

    public Integer selectWorkOrderCountByStatusAndUserId(Map<String, Object> map);

    public List<WorkOrderVo> selectMyWorkOrderByStatus(Map<String, Object> map);

    public Integer selectMyWorkOrderCountByStatus(Map<String, Object> map);

//    public List<WorkOrderVo> listSave(Map<String, Object> map);
//
//    public Integer listSaveCount(Map<String, Object> map);


    public List<WorkOrderVo> selectWatchList(Map<String, Object> map);

    public Integer selectWatchListCount(Map<String, Object> map);

    public Integer selectCanDealCount(Map<String, Object> map);

    public Integer saveSignInfo(Map<String, Object> map);

}
