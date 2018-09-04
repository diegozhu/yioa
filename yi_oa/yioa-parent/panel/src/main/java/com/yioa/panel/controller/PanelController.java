package com.yioa.panel.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.yioa.common.exception.YiException;
import com.yioa.common.util.CommonUtil;
import com.yioa.panel.domain.PanelVo;
import com.yioa.panel.service.PanelSendService;
import com.yioa.sys.domain.SysUser;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * Created by liangpengcheng on 2018/3/24.
 */
@RestController
@RequestMapping("/t_panel_order")
public class PanelController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PanelController.class);
    @Autowired
    private PanelSendService panelSendService;
    @RequestMapping(value = "/submit",method = RequestMethod.POST)
    public String panelorderSubmit(PanelVo panelVo, HttpServletRequest request) throws YiException {
        String userId = CommonUtil.getUserIdFromSession(request);
        SysUser user = CommonUtil.getUserLoginInfoFromSession(request);
        return this.panelSendService.orderSubmit(userId,user.getName(),panelVo);
    }

    @ApiOperation(value = "待办工单列表接口", notes = "此接口有一系列的查询,用status区分三个状态：处理中，已回单，已归档;返回参数包括：工单ID，工单名称，工单类型，派单人，派单时间等。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "工单状态", required = true, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页步长", required = false, dataType = "String")
    })
    @RequestMapping(value = "/listPanelOrder/{status}",method = RequestMethod.GET)
    public Page<PanelVo> listPanelOrder(@PathVariable String status, @RequestParam(required = false, defaultValue = "1") int current,
                                        @RequestParam(required = false, defaultValue = "2000") int size, @RequestParam(required = false, defaultValue = "") String keyword, HttpServletRequest request) throws YiException{
        String userId = CommonUtil.getUserIdFromSession(request);
        Page<PanelVo> list = this.panelSendService.listSave(status,userId,current,size,keyword);
        return list;
    }
}
