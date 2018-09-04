package com.yioa.common.util;

/**
 * 本来提示消息应该在配置文件中写的，这里简单处理下，放代码中
 *
 * Created by tao on 2017-05-22.
 *
 */
public final class SysErrorCnst {


    public static final String MOBILE_NOT_EXSITS = "YI_SYS_900001";

    public static final String MSG_MOBILE_NOT_EXSITS = "号码不存在";

    public static final String SESSION_EXPIRE = "YI_SYS_900002";

    public static final String MSG_SESSION_EXPIRE = "回话过期，请重新登录";


    public static final String FLOW_RUN_ERR = "YI_OA_800001";

    public static final String MSG_FLOW_RUN_ERR = "流程流转失败";



    public static final String COMMON_ERROR = "YI_OA_999999";

    public static final String MSG_COMMON_ERROR= "运行时异常，请跟踪日志";

    public static final String NO_CREATOR_ERROR = "YI_OA_100001";

    public static final String MSG_NO_CREATOR_ERROR= "创建人为空";



    public static final String FLOW_HANDLER_ORG_ERR = "YI_OA_800002";

    public static final String MSG_FLOW_HANDLER_ORG_ERR= "当前不支持ORG类型的处理人";

}
