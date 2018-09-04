package com.yioa.ssm.domain;

/**
 * 处理人信息，默认挂在环节的配置上
 * Created by tao on 2017-05-30.
 */
public class FlowHandlerVo {

    //固定执行人
    //支持多个处理人，以“,”分开
    private String handlerBy;

    //支持多个处理人，以“,”分开
    private String handlerById;

    //组织，角色，或者某个人  org role user
    private String handlerByType;


    /**
     * spel
     * 暂时只考虑支持与某环节相同
     */
    private String spelStr;

    /**
     * 是否使用spel
     */
    private String useSpel;


    public FlowHandlerVo() {
    }

    public FlowHandlerVo(String handlerBy, String handlerById, String handlerByType) {
        this.handlerBy = handlerBy;
        this.handlerById = handlerById;
        this.handlerByType = handlerByType;
    }


    public FlowHandlerVo(String spelStr) {
        this.spelStr = spelStr;
    }


    public String getUseSpel() {
        return useSpel;
    }

    public void setUseSpel(String useSpel) {
        this.useSpel = useSpel;
    }

    public String getSpelStr() {
        return spelStr;
    }

    public void setSpelStr(String spelStr) {
        this.spelStr = spelStr;
    }

    public String getHandlerBy() {
        return handlerBy;
    }

    public void setHandlerBy(String handlerBy) {
        this.handlerBy = handlerBy;
    }

    public String getHandlerById() {
        return handlerById;
    }

    public void setHandlerById(String handlerById) {
        this.handlerById = handlerById;
    }

    public String getHandlerByType() {
        return handlerByType;
    }

    public void setHandlerByType(String handlerByType) {
        this.handlerByType = handlerByType;
    }


    @Override
    public String toString() {
        return "FlowHandlerVo{" +
                "handlerBy='" + handlerBy + '\'' +
                ", handlerById='" + handlerById + '\'' +
                ", handlerByType='" + handlerByType + '\'' +
                '}';
    }
}
