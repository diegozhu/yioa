package com.yioa.oa.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yioa.common.exception.YiException;
import com.yioa.common.util.CommonUtil;
import com.yioa.oa.domain.TaskVo;
import com.yioa.oa.mapper.TaskMapper;
import com.yioa.oa.service.base.impl.EoaBaseService;
import com.yioa.oa.service.base.impl.Query;
import com.yioa.oa.util.DateHelper;
import com.yioa.sys.domain.SysUser;

@Service
public class TaskService extends EoaBaseService<TaskMapper, TaskVo> {

    private static final Logger logger = LoggerFactory.getLogger(WorkOrderServiceImpl.class);

    
    @Autowired
    private TaskMapper taskMapper;
    
    //@Autowired
    //private SysUserMapper sysUserMapper;
    
    
    /**
     * 存储草稿
     *
     * @param userId
     * @param userName
     * @param workOrderVo
     * @return
     * @throws YiException
     */
    public String saveOrUpdate(TaskVo taskVo, HttpServletRequest request) throws YiException{
    	logger.debug("save taskvo");
    	 String userId = CommonUtil.getUserIdFromSession(request);
         SysUser user = CommonUtil.getUserLoginInfoFromSession(request);
         EntityWrapper<TaskVo> entityWrapper = null;
         
         if(StringUtils.isNotBlank(taskVo.getTaskId())) {
            entityWrapper = new EntityWrapper<TaskVo>();
      		entityWrapper.eq("task_id", taskVo.getTaskId());
         }else {
             taskVo.setTaskId(CommonUtil.getUUID());
         }
         
         taskVo.setCreateBy(user.getName());
         taskVo.setCreateById(userId);
         taskVo.setCreateDate(new Date());
       	 taskVo.setStatus("未完成");
       	 taskVo.setIsDeleted("false");
       	 
       	if(StringUtils.isBlank(taskVo.getProjectName())) {
     		 taskVo.setProjectName("未命名");
     	 }
       	if(StringUtils.isBlank(taskVo.getAlertResult())) {
     		 taskVo.setAlertResult("none");
     	 }
       	if(StringUtils.isBlank(taskVo.getTaskLevel())) {
      		 taskVo.setTaskLevel("不重要-不紧急");
      	 }

       	 if(DateHelper.getDate(taskVo.getStartTime()).getTime() > DateHelper.getDate(taskVo.getEndTime()).getTime()) {
             throw new RuntimeException("结束时间不能早于开始时间");
       	 }
       	 
     	taskVo.setStartTime(DateHelper.formatDate(DateHelper.getDate(taskVo.getStartTime()), "yyyy-MM-dd HH:mm"));
    	taskVo.setAlertTime(DateHelper.formatDate(DateHelper.getDate(taskVo.getAlertTime()), "yyyy-MM-dd HH:mm"));
       	taskVo.setEndTime(DateHelper.formatDate(DateHelper.getDate(taskVo.getEndTime()), "yyyy-MM-dd HH:mm"));
       	 
      	if(StringUtils.isEmpty(taskVo.getStatus())) {
            throw new RuntimeException("状态不能为空");
      	}
      	
        boolean bool = entityWrapper != null ? this.update(taskVo, entityWrapper) : this.insert(taskVo);
        if (!bool) {
            throw new RuntimeException("存储任务错误");
        }
        return "1";
    }
	
	public Page<TaskVo> getMineTasks(Map<String, String> params, HttpServletRequest request) throws YiException {
				
		String userId = CommonUtil.getUserIdFromSession(request);
	    logger.debug("userid" + userId);
        
		EntityWrapper<TaskVo> entityWrapper = new EntityWrapper<TaskVo>();
		
		if(StringUtils.isBlank(params.get("start"))) {
			throw new RuntimeException("start日期不能为空");
		}
		if(StringUtils.isBlank(params.get("end"))) {
			throw new RuntimeException("end日期不能为空");
		}
		for(String searchKey : Arrays.asList("task_name")) {
			if(params.get(searchKey) != null && StringUtils.isNotBlank(params.get(searchKey))) {
				entityWrapper.like(searchKey, params.get(searchKey).toString());
			}
		}
		for(String searchKey : Arrays.asList("task_level", "project_name", "status")) {
			if(params.get(searchKey) != null && StringUtils.isNotBlank(params.get(searchKey))) {
				entityWrapper.eq(searchKey, params.get(searchKey).toString());
			}
		}
		String dayStart = DateHelper.formatDate(DateHelper.getDate(params.get("start")), "yyyy-MM-dd HH:mm");
		String dayEnd = DateHelper.formatDate(DateHelper.getDate(params.get("end")), "yyyy-MM-dd HH:mm");
		
		String andSql = " and (( start_time >= {0} && start_time <= {1})";
		andSql += "       or ( end_time >= {2} && end_time <= {3})";
		andSql += "       or ( start_time <=  {4} && end_time >= {5}))";
			 
			
		entityWrapper.eq("create_by_id", userId).and(" is_deleted = 'false' " + andSql, dayStart, dayEnd, dayStart, dayEnd, dayStart, dayEnd );

		List<TaskVo> res = this.selectList(entityWrapper);
		Page<TaskVo> page = new Page<>();
		
		page.setRecords(res);

        return this.selectPage(new Query<TaskVo>(params).getPage(), entityWrapper);
	}

	public String setTaskStatus(TaskVo task, String status, HttpServletRequest request, String taskId) throws Exception {
		TaskVo taskVo = this.selectById(taskId);
		String userId = CommonUtil.getUserIdFromSession(request);
		if(!userId.equals(taskVo.getCreateById())) {
			throw new Exception("不能修改别人的任务");
		}
		taskVo.setStatus(status);
		
		EntityWrapper<TaskVo> entityWrapper = new EntityWrapper<TaskVo>();
		entityWrapper.eq("task_id", taskId);
		this.taskMapper.update(taskVo, entityWrapper);
		return "ok";
	}
	
	public TaskVo get(HttpServletRequest request, String taskId) throws Exception {
		TaskVo taskVo = this.selectById(taskId);
		return taskVo;
	}

	public String delete(HttpServletRequest request, String taskId) throws Exception {
		TaskVo taskVo = this.selectById(taskId);
		String userId = CommonUtil.getUserIdFromSession(request);
		if(!userId.equals(taskVo.getCreateById())) {
			throw new Exception("不能修改别人的任务");
		}
		taskVo.setIsDeleted("true");
		
		EntityWrapper<TaskVo> entityWrapper = new EntityWrapper<TaskVo>();
		entityWrapper.eq("task_id", taskId);
		this.taskMapper.update(taskVo, entityWrapper);
		return "ok";
	}

	public List<Map<String, Object>> getByProject(Map<String, String> params, HttpServletRequest request) throws YiException {
		String userId = CommonUtil.getUserIdFromSession(request);
	    logger.debug("userid" + userId);
        
		String start = DateHelper.formatDate(DateHelper.getDate(params.get("start")), "yyyy-MM-dd HH:mm");
		String end = DateHelper.formatDate(DateHelper.getDate(params.get("end")), "yyyy-MM-dd HH:mm");

	    String sql = "select project_name,count(status) as count, status from t_my_task where is_deleted ='false' and create_by_id = '"+userId+"'";
	    
	    sql += " and (( start_time >=  \""+ start +"\" && start_time <=  \""+ end +"\")";
	    sql += " or ( end_time >=  \""+ start +"\" && end_time <=  \""+ end +"\")";
	    sql += " or ( start_time <=   \""+ start +"\" && end_time >=   \""+ end +"\"))";
	    if(StringUtils.isNotBlank(params.get("project_name"))) {
	    	sql += " and project_name = \"" + params.get("project_name") + "\"";
	    }
		sql += "group by project_name, status";
		
	    List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql );
        return result;
	}
	
	public List<TaskVo> getForAlert(String alertTime){
		EntityWrapper<TaskVo> entityWrapper = new EntityWrapper<TaskVo>();
		entityWrapper.and(" is_deleted = 'false' and alert_result = 'none' and status = '未完成' and alert_time <= \""+alertTime+"\" ");
		List<TaskVo> res = this.selectList(entityWrapper);
		return res;
	}

	public Page<TaskVo> listByStartDate(Map<String, String> params, HttpServletRequest request) throws YiException {
		String userId = CommonUtil.getUserIdFromSession(request);
	    logger.debug("userid" + userId);
        
		EntityWrapper<TaskVo> entityWrapper = new EntityWrapper<TaskVo>();
		
		if(StringUtils.isBlank(params.get("start"))) {
			throw new RuntimeException("start日期不能为空");
		}
		if(StringUtils.isBlank(params.get("end"))) {
			throw new RuntimeException("end日期不能为空");
		}
		for(String searchKey : Arrays.asList("task_name")) {
			if(params.get(searchKey) != null && StringUtils.isNotBlank(params.get(searchKey))) {
				entityWrapper.like(searchKey, params.get(searchKey).toString());
			}
		}
		for(String searchKey : Arrays.asList("task_level", "project_name", "status")) {
			if(params.get(searchKey) != null && StringUtils.isNotBlank(params.get(searchKey))) {
				entityWrapper.eq(searchKey, params.get(searchKey).toString());
			}
		}
		String dayStart = DateHelper.formatDate(DateHelper.getDate(params.get("start")), "yyyy-MM-dd HH:mm");
		String dayEnd = DateHelper.formatDate(DateHelper.getDate(params.get("end")), "yyyy-MM-dd HH:mm");
			
		entityWrapper.eq("create_by_id", userId).and(" is_deleted = 'false' and ( start_time >= {0} && start_time <= {1}) ", dayStart, dayEnd);

		List<TaskVo> res = this.selectList(entityWrapper);
		Page<TaskVo> page = new Page<>();
		
		page.setRecords(res);

        return this.selectPage(new Query<TaskVo>(params).getPage(), entityWrapper);
	}
}
