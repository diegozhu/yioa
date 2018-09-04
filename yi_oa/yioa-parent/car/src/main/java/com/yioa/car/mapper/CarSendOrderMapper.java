package com.yioa.car.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yioa.car.domain.CarSendOrderVo;
import com.yioa.car.domain.CarVo;

/**
 * Created by tao on 2017-06-04.
 */
public interface CarSendOrderMapper extends BaseMapper<CarSendOrderVo> {

    public List<CarSendOrderVo> selectOrderByStatusAndUserId(Map<String, Object> map);

    public Integer selectOrderCountByStatusAndUserId(Map<String, Object> map);

    public List<CarSendOrderVo> selectWatchList(Map<String, Object> map);

    public Integer selectWatchListCount(Map<String, Object> map);


    /**
     * 我派出的派车
     * @param map
     * @return
     */
    public List<CarSendOrderVo> selectMyOrderByStatus(Map<String, Object> map);


    /**
     * 我派出的派车数量
     * @param map
     * @return
     */
    public Integer selectMyOrderCountByStatus(Map<String, Object> map);


    /**
     * 查看车子
     *
     * @param map
     * @return
     */
    public List<CarVo> selectCar(Map<String, Object> map);

    public Integer selectCanDealCount(Map<String, Object> map);

	public List<CarSendOrderVo> selectOrderByStatus(Map<String, Object> map);
}
