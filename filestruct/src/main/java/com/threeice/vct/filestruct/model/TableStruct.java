package com.threeice.vct.filestruct.model;

import com.threeice.vct.core.enums.GeometryTypeEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TableStruct {
    private String tableName;
    private int filedCount;
    private List<VctFiled> fileds;

    //辅助字段，要素代码
    private String featureCode;
    //辅助字段，图形类型
    private GeometryTypeEnum geometryTypeEnum;


    public TableStruct(String tableName,int filedCount){
        this.tableName=tableName;
        this.filedCount=filedCount;
        fileds=new ArrayList<>(filedCount);
    }
}
