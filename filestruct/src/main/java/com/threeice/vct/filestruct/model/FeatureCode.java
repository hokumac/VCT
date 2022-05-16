package com.threeice.vct.filestruct.model;

import com.threeice.vct.core.enums.GeometryTypeEnum;
import lombok.Data;

@Data
public class FeatureCode {
    private String featureCode;
    private String featureName;
    private GeometryTypeEnum geometryType;
    private String tableName;

    public FeatureCode(String featureCodeLine){
        String[] split = featureCodeLine.split("\\,");
        //2003030300,主要矿产储藏区,Polygon,ZYKCCCQ
        featureCode =split[0];
        featureName =split[1];
        geometryType=getGeometryType(split[2]);
        tableName=split[3];
    }

    private GeometryTypeEnum getGeometryType(String type){
        return GeometryTypeEnum.Line;
    }
}
