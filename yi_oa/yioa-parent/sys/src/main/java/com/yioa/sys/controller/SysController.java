package com.yioa.sys.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.yioa.common.exception.YiException;
import com.yioa.common.util.CommonUtil;
import com.yioa.common.util.PageUtil;
import com.yioa.common.util.SysErrorCnst;
import com.yioa.sys.domain.SysOffice;
import com.yioa.sys.domain.SysRole;
import com.yioa.sys.domain.SysUser;
import com.yioa.sys.mapper.SysOfficeMapper;
import com.yioa.sys.mapper.SysUserMapper;
import com.yioa.sys.service.FileUploadService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Created by tao on 2017-05-28.
 */

@RequestMapping("/sys")
@RestController
public class SysController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysController.class);

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysOfficeMapper sysOffice;

    @Autowired
    private FileUploadService fileUploadService;



    @ApiOperation(value = "人员查询接口", notes = "查询所有人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "exStr", value = "已选人员，需要排除", required = false, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页步长", required = false, dataType = "String")
    })
    @RequestMapping(value = "/staff/list/{exStr}", method = RequestMethod.GET)
    public Page<SysUser> staffList(@PathVariable(required = false, value = "") String exStr, @RequestParam(required = false, defaultValue = "1") int current, @RequestParam(required = false, defaultValue = "20") int size, HttpServletRequest request) {

        int offset = PageUtil.getOffset(current, size);
        Map<String, Object> tMap = Maps.newHashMap();

        CommonUtil.setExIdStr(exStr, tMap);
        Page<SysUser> page = new Page<>();
        List<SysUser> list = this.sysUserMapper.querySysUserByKeyword(tMap);

        int total = this.sysUserMapper.querySysUserCntByKeyword(tMap);
        page.setRecords(list);
        page.setCondition(tMap);
        page.setCurrent(current);
        page.setSize(size);
        page.setTotal(total);
        LOGGER.info("#### staffList return {}", page);
        return page;
    }


    @ApiOperation(value = "人员查询接口-过滤", notes = "查询所有人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "exStr", value = "已选人员，需要排除", required = false, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "搜索关键字", required = true, dataType = "String"),
            @ApiImplicitParam(name = "maxNum", value = "最大返回跳数，不超过100", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/staff/q/{keyword}/{exStr}/{maxNum}", method = RequestMethod.GET)
    public List<SysUser> staffListBykey(@PathVariable String keyword, @PathVariable(required = false, value = "") String exStr, @PathVariable int maxNum, HttpServletRequest request) {
        maxNum = maxNum > 100 ? 100 : maxNum;

        Map<String, Object> map = ImmutableMap.of("maxNum", maxNum, "login_name", keyword, "name", keyword, "phone", keyword, "mobile", keyword);

        Map<String, Object> tMap = Maps.newHashMap();
        tMap.putAll(map);
        CommonUtil.setExIdStr(exStr, tMap);

        List<SysUser> list = this.sysUserMapper.querySysUserByKeyword(tMap);

        return list;
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Map<String ,String> uploadLogo(@RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile, HttpServletRequest request) throws YiException {

        String userId = CommonUtil.getUserIdFromSession(request);

        Map<String, String> map = null;
        try {
            map = this.fileUploadService.saveFile(uploadFile, userId);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("");
            throw new YiException(SysErrorCnst.COMMON_ERROR, SysErrorCnst.MSG_COMMON_ERROR);
        }
        if (map == null || map.isEmpty()) {
            return ImmutableMap.of();
        }

        return map;
    }


    @RequestMapping(value = "/staff/{id}", method = RequestMethod.GET)
    public SysUser queryStaff(@PathVariable(required = false, value = "") String id, HttpServletRequest request) throws YiException {

        if (StringUtils.isEmpty(id) || "xx".equalsIgnoreCase(id)) {
            id = CommonUtil.getUserIdFromSession(request);
        }
        Assert.notNull(id, "id should not empty : " + id);
        Map<String, Object> map = ImmutableMap.of("id", id);

        List<SysUser> list = this.sysUserMapper.querySysUserByKeyword(map);

        Assert.notEmpty(list, "query should not empty: " + list);

        SysUser user = list.get(0);

        Map<String, Object> tMap = ImmutableMap.of("userId", id);
        List<SysRole> roleList = this.sysUserMapper.queryRoleByUserId(tMap);

        user.setRoleList(roleList);
        return user;
    }

    @RequestMapping(value = "/login/{id}", method = RequestMethod.GET)
    public String queryLogin(@PathVariable(required = false, value="") String id, HttpServletRequest request) throws YiException {
        if(StringUtils.isEmpty(id) || "xx".equalsIgnoreCase(id)) {
            id = CommonUtil.getLoginStateFromSession(request);
        }
        return id;
    }

    @RequestMapping(value = "/qryOffice/{id}", method = RequestMethod.GET)
    public List<SysOffice> qryOffice(@PathVariable(required = false, value="") String id, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        List<SysOffice> office = this.sysOffice.queryOffice(map);
        return office;
    }

    @RequestMapping(value = "/qryDepart/{id}", method = RequestMethod.GET)
    public List<SysOffice> qryDepart(@PathVariable(required = false, value="") String id, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        List<SysOffice> office = this.sysOffice.queryDepart(map);
        return office;
    }

    /**
     * 绑定
     * @param username
     * @param no
     *  @param companyId
     *   @param officeId
     * @param request
     * @return
     * @throws YiException
     */
    @RequestMapping(value = "/bindAccount/{username}/{no}/{companyId}/{officeId}",method = RequestMethod.GET)
    public SysUser bindAccount(@PathVariable String username, @PathVariable String no,@PathVariable String companyId,@PathVariable String officeId, HttpServletRequest request)throws YiException {
        System.out.println("用户名是" + username + "手机号码是" + no + "所属公司" + companyId + "所属部门" + officeId);
        String id = CommonUtil.getUserIdFromSession(request);
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("username",username);
        map.put("no",no);
        map.put("companyId",companyId);
        map.put("officeId",officeId);
        this.sysOffice.bindAccount(map);
        SysUser user = new SysUser();
        user = user.selectOne(new EntityWrapper<SysUser>().eq("name", username));
        return user;
    }

}
