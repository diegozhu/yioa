package com.yioa.market.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yioa.market.domain.MarketOrderVo;

/**
 * Created by liangpengcheng on 2018/5/5.
 */
public interface MarketOrderMapper extends BaseMapper<MarketOrderVo> {

    public List<MarketOrderVo> selectMarketOrderByStatusAndUserId(Map<String, Object> map);

    public Integer selectMarketOrderCountByStatusAndUserId(Map<String,Object> map);

    public List<MarketOrderVo> selectMyMarketOrderByStatus(Map<String,Object> map);

    public Integer selectMyMarketOrderCountByStatus(Map<String, Object> map);

    public List<MarketOrderVo> selectWatchList(Map<String,Object> map);

    public Integer selectWatchListCount(Map<String,Object> map);

    public Integer selectCanDealCount(Map<String, Object> map);

    public List<MarketOrderVo> qryMarketList(Map<String,Object> map);

    public Integer qryMarketListCount(Map<String,Object> map);

    /**
     * 我派出的派车
     * @param map
     * @return
     */
    public List<MarketOrderVo> selectMyOrderByStatus(Map<String, Object> map);

    /**
     * 我派出的派车数量
     * @param map
     * @return
     */
    public Integer selectMyOrderCountByStatus(Map<String, Object> map);

	public List<MarketOrderVo> selectMarketOrderByStatus(Map<String, Object> map);
}
