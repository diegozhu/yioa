
create table t_user_openid (
  id varchar(64),
  login_name varchar(64),
  openid varchar(64)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;


DROP TABLE IF EXISTS t_work_order;
create table t_work_order (
  work_order_id varchar(64),
  work_order_code varchar(64),
  organiser varchar(640),
  organiser_name varchar(640),
  co_organiser varchar(640),
  co_organiser_name varchar(640),

  work_order_type varchar(10),
  emergency_level VARCHAR(10) DEFAULT '低' ,
  work_order_subject varchar(64),
  work_order_content varchar(2000),
  milestone_date datetime  COMMENT '阶段回单时间',
  req_complete_date datetime  COMMENT '要求完成时间',
  attach_file varchar(1000),
  create_by_id varchar(64) NOT NULL COMMENT '创建人id',
  create_by varchar(64) NOT NULL COMMENT '创建人',
  create_date datetime NOT NULL COMMENT '创建时间',

  status varchar(20) DEFAULT "doing" COMMENT '存储工单是否归档,doing,done' ,
  
  editable varchar(2) DEFAULT "1" COMMENT '存储工单是否可以编辑，首次收单后改为0，不再改回' ,

  flow_Info_Id varchar(64) COMMENT '当前所处环节的流程id',
  flow_stage varchar(20) DEFAULT "STATES_STRAT",
  flow_stage_name varchar(40)
  

) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;



DROP  TABLE  t_flow_info;
create table t_flow_info (
  flow_Info_Id varchar(64),
  work_order_id varchar(64),
  prev_flow_Info_Id varchar(64),
  flow_stage varchar(64),
  flow_stage_name varchar(64),
  attach_file varchar(1000),

  handle_by_id varchar(64)  COMMENT '处理人id',
  handle_by varchar(20) COMMENT '处理人',
  handle_by_type varchar(20) COMMENT '处理人类型 org user role',

  receive_user_id varchar(64),
  recieve_date datetime,

  complete_user_id varchar(64),
  complete_user_name varchar(20),

  complete_date datetime,
  create_date datetime,
  notes varchar(2000)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;


ALTER  table sys_user ADD COLUMN driver_flag VARCHAR(10);


DROP  TABLE t_car;
create table t_car (
  car_id varchar(64),
  car_no varchar(64),
  car_type varchar(200),
  capacity TINYINT,
  delete_tag varchar(2) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;


DROP TABLE IF EXISTS t_car_send_order;
create table t_car_send_order (
  car_send_order_id varchar(64),
  car_send_order_code varchar(64),
  driver_id varchar(64),
  driver_name varchar(64),
  car_id varchar(64),
  car_no varchar(64),
  passenger_id varchar(640),
  passenger_name varchar(640),
  feedback_id varchar(640),
  feedback_name varchar(640),
  destination varchar(200),
  plan_leave_date datetime,
  plan_back_date datetime,
  car_send_order_subject varchar(300),
  car_send_order_content varchar(2000),
  attach_file varchar(1000),
  
  create_by_id varchar(20) NOT NULL COMMENT '创建人id',
  create_by varchar(20) NOT NULL COMMENT '创建人',
  create_date datetime NOT NULL COMMENT '创建时间',
  status varchar(20) DEFAULT "doing" COMMENT '存储是否归档,doing,done' ,
  editable varchar(2) DEFAULT "1" COMMENT '存储工单是否可以编辑，首次收单后改为0，不再改回' ,
  flow_Info_Id varchar(64) COMMENT '当前所处环节的流程id',
  flow_stage varchar(20) DEFAULT "STATES_STRAT",
  flow_stage_name varchar(40) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

DROP TABLE IF EXISTS t_watch_order;
create table t_watch_order (
  watch_id varchar(64),
  user_id varchar(64),
  order_id varchar(64),
  create_date datetime NOT NULL COMMENT '创建时间',
  delete_date datetime,
  delete_state varchar(10) DEFAULT "0"
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;


