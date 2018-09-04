package com.yioa.ssm.util;

public enum TaskFlows {
	
  		FlOW_INIT("新创建","新创建"),
  		FlOW_START("开始","开始"),   
	    FLOW_DONE("结束","结束");        

	    private String name;

	    private String status;

	    private TaskFlows(String name, String status) {
	        this.name = name;
	        this.status = status;
	    }

	    public String getName() {
	        return name;
	    }

	    public String getStatus() {
	        return status;
	    }
}
