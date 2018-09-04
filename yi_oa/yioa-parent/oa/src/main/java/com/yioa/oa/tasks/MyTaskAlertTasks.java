package com.yioa.oa.tasks;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xseed.sms.service.SmsService;
import com.yioa.oa.controller.WorkorderController;
import com.yioa.oa.domain.TaskVo;
import com.yioa.oa.service.impl.TaskService;
import com.yioa.oa.util.DateHelper;
import com.yioa.sys.domain.SysUser;
import com.yioa.sys.mapper.SysUserMapper;

@Component
public class MyTaskAlertTasks {

    private static final Logger logger = LoggerFactory.getLogger(WorkorderController.class);
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
    private SmsService smsService;
    
    @Scheduled(fixedRate = 60000) //每分钟
    public void reportCurrentTime() {

    	logger.error("现在时间：" + DateHelper.formatDate(new Date()));
    	String now = DateHelper.formatDate(new Date(), "yyyy-MM-dd HH:mm");
    	List<TaskVo> tasks = taskService.getForAlert(now);
    	
    	List<String> userIds = tasks.stream().map(TaskVo::getCreateById).collect(Collectors.toList());
    	if(userIds == null || userIds.isEmpty()) {
    		logger.error("没有需要提醒的");
    		return ;
    	}
    	List<SysUser> users = sysUserMapper.selectBatchIds(userIds);
    	
    	Map<String, SysUser> userMap = users.stream().collect(Collectors.toMap(SysUser::getId, Function.identity()));
    	
    	for(TaskVo task : tasks) {
    		SysUser user = userMap.get(task.getCreateById());
    		if(user == null || StringUtils.isBlank(user.getMobile())) {
    			logger.error("task has no user taskId: " + task.getTaskId() + " createById:"+ task.getCreateById());
    			continue;
    		}
    		String mobile = StringUtils.isBlank(user.getMobile()) ? user.getPhone() : user.getMobile();
    		if(StringUtils.isBlank(mobile)) {
    			logger.error("user has mno phone or mobile" + user.getName() + "/" + user.getId());
    			continue;
    		}
    		smsService.sendSms("您"+task.getStartTime()+"的任务【" + task.getTaskName() + "】即将在" + task.getAlert().replace("提前", "") + "后开始，请关注。", mobile);
  			task.setAlertResult("ok/" + now);
			taskService.updateAllColumnById(task);
    	}
    }

}
