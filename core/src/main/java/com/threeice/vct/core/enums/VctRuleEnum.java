package com.threeice.vct.core.enums;


public enum VctRuleEnum {

    PRESENT("PRESENT", "国土VCT3.0标准"),
    PLAN("plan", "土地利用规划标准")
    ;


    private String code;

    /**
     * 说明
     */
    private String desc;

    VctRuleEnum(String mode, String desc) {
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
