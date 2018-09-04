package com.yioa.oa.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yioa.oa.domain.TaskVo;

/**
 * Created by tao on 2017-05-23.
 */
public interface TaskMapper extends BaseMapper<TaskVo> {


	 public TaskVo selectByTaskId(String taskId);
}
