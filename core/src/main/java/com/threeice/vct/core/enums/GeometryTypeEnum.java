package com.threeice.vct.core.enums;


public enum GeometryTypeEnum {

    Point("Point", "点"),
    Line("Line", "线"),
    Polygon("Polygon", "面"),
    Annotation("Annotation", "注记"),
    Table("Table", "属性表"),
    ;


    private String code;

    /**
     * 说明
     */
    private String desc;

    GeometryTypeEnum(String mode, String desc) {
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
