package com.yioa.oa.controller;

import java.util.List;
import java.util.Map;

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
import com.yioa.oa.domain.TaskVo;
import com.yioa.oa.service.impl.TaskService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/oa/task")
public class MyTasksController {



    private static final Logger logger = LoggerFactory.getLogger(WorkorderController.class);

    @Autowired
    private TaskService taskService;

//    @Autowired
//    private FlowInfoMapper flowInfoMapper;
//
//    @Autowired
//    private CarSendOrderMapper carSendOrderMapper;
//
//    @Autowired
//    private MarketOrderMapper marketOrderMapper;
//    @Autowired
//    private FlowService flowService;
//
//    @Autowired
//    private SysUserMapper sysUserMapper;
//    @Autowired
//    private MarketSendService marketSendService;
//
//    @Autowired
//    private EventBus eventBus;

    
    
    @ApiOperation(value = "创建task保存", notes = "创建task保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskVo", value = "任务信息", required = true, dataType = "taskVo")
    })
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public String saveOrUpdate(TaskVo task, HttpServletRequest request) throws YiException {
    	logger.debug("save", task);
        return this.taskService.saveOrUpdate(task, request);
    }
    
    @RequestMapping(value = "/done/{taskId}", method = RequestMethod.POST)
    public String setTaskDone(TaskVo task, HttpServletRequest request, @PathVariable String taskId) throws Exception {
    	logger.debug("setTaskDone", task);
        return this.taskService.setTaskStatus(task, "已完成", request, taskId);
    }
    
    @RequestMapping(value = "/undone/{taskId}", method = RequestMethod.POST)
    public String setTaskUndone(TaskVo task, HttpServletRequest request, @PathVariable String taskId) throws Exception {
    	logger.debug("setTaskDone", task);
        return this.taskService.setTaskStatus(task, "未完成", request, taskId);
    }
    
    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    public TaskVo get(HttpServletRequest request, @PathVariable String taskId) throws Exception {
        return this.taskService.get(request, taskId);
    }
    
    @RequestMapping(value = "/delete/{taskId}", method = RequestMethod.POST)
    public String delete(HttpServletRequest request, @PathVariable String taskId) throws Exception {
        return this.taskService.delete(request, taskId);
    }

    @ApiOperation(value = "列出task", notes = "列出task")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态", required = true, dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页步长", required = false, dataType = "String")
    })

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Page<TaskVo> list(@RequestParam Map<String, String> params, HttpServletRequest request) throws YiException {
        Page<TaskVo> list = taskService.getMineTasks(params, request);
        return list;
    }
    
    @RequestMapping(value = "/list-by-start-date", method = RequestMethod.GET)
    public Page<TaskVo> listByStartDate(@RequestParam Map<String, String> params, HttpServletRequest request) throws YiException {
        Page<TaskVo> list = taskService.listByStartDate(params, request);
        return list;
    }
    
    @RequestMapping(value = "/list/by-project", method = RequestMethod.GET)
    public List<Map<String, Object>> getByProject(@RequestParam Map<String, String> params, HttpServletRequest request) throws YiException {
        List<Map<String, Object>> list = taskService.getByProject(params, request);
        return list;
    }
}
