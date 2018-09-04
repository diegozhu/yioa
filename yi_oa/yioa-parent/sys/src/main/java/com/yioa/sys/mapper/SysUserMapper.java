package com.yioa.sys.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yioa.sys.domain.SysRole;
import com.yioa.sys.domain.SysUser;

import java.util.List;
import java.util.Map;

/**
 * Created by tao on 2017-05-22.
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    public List<SysUser> querySysUserByKeyword(Map<String, Object> map);

    public Integer querySysUserCntByKeyword(Map<String, Object> map);

    public List<SysRole> queryRoleByUserId(Map<String, Object> map);

    public List<SysUser> queryUserByRoleId (Map<String, Object> map);

}
