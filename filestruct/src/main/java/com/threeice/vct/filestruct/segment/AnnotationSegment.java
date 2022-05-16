package com.threeice.vct.filestruct.segment;


import com.threeice.vct.core.ISystemLog;
import com.threeice.vct.core.enums.ErrorLevelEnum;
import com.threeice.vct.core.enums.VctPartEnum;
import com.threeice.vct.filestruct.IGeometryCheckCursor;

public class AnnotationSegment extends VctGeometrySegmentBase<AnnotationSegment> {
    public AnnotationSegment(ISystemLog systemLog) {
        super(systemLog);
    }

    @Override
    public int checkGeometry(double maxx, double maxy, double minx, double miny) {
        String bsm ="";
        try {
            //标识码
            bsm = readLine();
            //要素代码
            readLine();
            //图形表现编码
            readLine();
            //注记的特征类型
            readLine();
            //注记内容
            readLine();
            //坐标
            checkExtent(readLine(),bsm,maxx,  maxy,  minx,  miny);
            //角度
            readLine();
            //读取结尾的0
            readLine();
            return 1;
        }catch (Exception ex){
            addErrorRecord("读取"+getVctPartEnum().getDesc()+"中bsm="+bsm+"数据行异常！", bsm,ErrorLevelEnum.SERIOUS);
            systemLog.error(ex);
            if(skipToNextRecord()) {
                return 2;
            }
            return -1;
        }
    }


    @Override
    public VctPartEnum getVctPartEnum() {
        return VctPartEnum.Annotation;
    }


}
