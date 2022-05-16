package com.threeice.vct.filestruct.segment;

import com.threeice.vct.core.ISystemLog;
import com.threeice.vct.core.check.ICheckRecord;
import com.threeice.vct.core.enums.ErrorLevelEnum;
import com.threeice.vct.core.enums.VctPartEnum;
import com.threeice.vct.core.enums.VctRuleEnum;
import com.threeice.vct.core.model.CheckErrorInfo;
import com.threeice.vct.filestruct.IGeometryCheckCursor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public abstract class VctGeometrySegmentBase<T> extends VctSegmentBase<T> implements IGeometryCheckCursor<PolygonSegment> {

    public VctGeometrySegmentBase(ISystemLog systemLog) {
        super(systemLog);
    }


    @Override
    public int checkGeometry(double maxx, double maxy, double minx, double miny) {
        return 1;
    }
    public boolean seekBegin(String tablename, VctPartEnum vctPartEnum) {
        String tempString = null;
        // 一次读入一行，直到读入null为文件结束
        while ((tempString = readLine()) != null) {
            if(!tempString.equals(getVctPartEnum().getCode()+"End")){
                return false;
            }

            if(tempString.equals(tablename))
            {
                return true;
            }
        }
        return false;
    }

    protected boolean checkExtent(String ptLine,String bsm,double maxx, double maxy, double minx, double miny){
        String [] pts =ptLine.split("\\,");
        Double x =Double.valueOf(pts[0]);
        Double y =Double.valueOf(pts[1]);

        if(x>maxx||x<minx||y>maxy||y<miny){
            addErrorRecord("BSM:"+bsm+"的点超出范围！", null,ErrorLevelEnum.SERIOUS);
            return false;
        }
        return true;
    }
}
