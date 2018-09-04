package com.yioa.common.util;


public interface IFlowInfo {
    public void setFlowStage(String id);
    public void setFlowStageName(String flowName);
    public void setStatus(String status);
    public void setFlowInfoId(String flowInfoId);
    public boolean updateById();
	public String getFlowStage();
	public String getFlowInfoId();
}
