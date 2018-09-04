package com.yioa.panel.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yioa.panel.domain.PanelVo;

import java.util.List;
import java.util.Map;

/**
 * Created by liangpengcheng on 2018/3/24.
 */
public interface PanelOrderMapper extends BaseMapper<PanelVo> {

    public List<PanelVo> selectPanelOrderByStatusAndUserId(Map<String, Object> map);

    public Integer selectPanelOrderCountByStatusAndUserId(Map<String,Object> map);

    public List<PanelVo> selectMyPanelOrderByStatus(Map<String,Object> map);

    public Integer selectMyPanelOrderCountByStatus(Map<String, Object> map);

    public List<PanelVo> selectWatchList(Map<String,Object> map);

    public Integer selectWatchListCount(Map<String,Object> map);

    public Integer selectCanDealCount(Map<String, Object> map);

}
