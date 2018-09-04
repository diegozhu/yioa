package com.yioa.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yioa.oa.domain.WatchOrderVo;
import com.yioa.sys.domain.SysRole;

import java.util.List;
import java.util.Map;

/**
 * Created by tao on 2017-06-29.
 */
public interface WatchOrderMapper extends BaseMapper<WatchOrderVo> {
    public List<SysRole> queryRoleByUserId(Map<String, Object> map);
}
