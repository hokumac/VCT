package com.threeice.vct.filestruct.segment;

import com.threeice.vct.core.ISystemLog;
import com.threeice.vct.core.enums.ErrorLevelEnum;
import com.threeice.vct.core.enums.VctPartEnum;
import com.threeice.vct.filestruct.model.FeatureCode;

import java.util.HashMap;

public class FeatureCodeSegment extends VctSegmentBase<FeatureCodeSegment>{
    public FeatureCodeSegment(ISystemLog systemLog) {
        super(systemLog);
    }

    private HashMap<String, FeatureCode> featureCodeHashMap;

    @Override
    public VctPartEnum getVctPartEnum() {
        return VctPartEnum.FEATURECODE;
    }

    @Override
    public boolean initSegment() {
        try {
            featureCodeHashMap = new HashMap<>();
            String first = readLine();
            while (first != null && first != "FeatureCodeEnd") {
                FeatureCode featureCode = new FeatureCode(first);
                first = readLine();
                featureCodeHashMap.put(featureCode.getTableName(), featureCode);
            }
            return featureCodeHashMap.size()>0;
        }catch (Exception ex){
            systemLog.error(ex);
            addErrorRecord("读取要素代码数据异常！",null, ErrorLevelEnum.DEADLY);
            return featureCodeHashMap.size()>0;
        }
    }

    public FeatureCode getFeatureCode(String tableName){
        if(featureCodeHashMap.containsKey(tableName)){
            return featureCodeHashMap.get(tableName);
        }
        return null;
    }

}
