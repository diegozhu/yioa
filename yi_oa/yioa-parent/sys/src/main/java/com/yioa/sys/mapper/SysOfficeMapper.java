package com.yioa.sys.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yioa.sys.domain.SysOffice;

import java.util.List;
import java.util.Map;

/**
 * Created by liangpengcheng on 2018/3/10.
 */
public interface SysOfficeMapper extends BaseMapper<SysOffice> {

    public List<SysOffice> queryOffice(Map<String, Object> map);

    public List<SysOffice> queryDepart(Map<String, Object> map);

    public void bindAccount(Map<String,Object> map);
}
