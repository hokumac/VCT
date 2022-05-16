package com.threeice.vct.core.enums;


public enum ErrorLevelEnum {

    DEADLY("100000", "致命问题"),
    SERIOUS("20000", "严重问题")
    ;

    /**
     * 模式，1: 停车点模式；2禁停区模式；3:仅开放B端停车区模式；4:同时开放B、C端停车区模式
     */
    private String code;

    /**
     * 说明
     */
    private String desc;

    ErrorLevelEnum(String mode, String desc) {
        this.code = mode;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
