package com.threeice.vct.core.enums;


public enum VctPartEnum {

    HEAD("Head", "文件头"),
    FEATURECODE("FeatureCode", "要素代码"),
    TableStructure("TableStructure", "表结构"),
    Point("Point", "点"),
    Line("Line", "线"),
    Polygon("Polygon", "面"),
    Annotation("Annotation", "注记"),
    Attibute("Attibute", "属性"),
    VarBin("VarBin", "字符"),
    None("None", "未知"),
    ;


    private String code;

    /**
     * 说明
     */
    private String desc;

    VctPartEnum(String mode, String desc) {
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
