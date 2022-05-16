package com.threeice.vct.filestruct.segment;

import com.threeice.vct.core.ISystemLog;
import com.threeice.vct.core.check.ICheckRecord;
import com.threeice.vct.core.enums.ErrorLevelEnum;
import com.threeice.vct.core.enums.VctPartEnum;
import com.threeice.vct.core.enums.VctRuleEnum;
import com.threeice.vct.filestruct.IGeometryCheckCursor;
import com.threeice.vct.filestruct.refline.RefLineHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PolygonSegment extends VctGeometrySegmentBase<PolygonSegment> {

    private static final String SPLITZERO="0";
    private static final int MINPOINTS=3;

    private RefLineHelper refLineHelper;

    public PolygonSegment(ISystemLog systemLog) {
        super(systemLog);
    }

    @Override
    public int checkGeometry(double maxx, double maxy, double minx, double miny) {
        String bsm="";
        try {
            //标识码
            bsm = readLine();
            //要素代码
            readLine();
            //图形表现编码
            readLine();
            //面的特征类型
            readLine();
            //标识点X坐标,标识点Y坐标
            String s = readLine();
            String[] pts = s.split("\\,");
            Double x = Double.valueOf(pts[0]);
            Double y = Double.valueOf(pts[1]);
            if (x > maxx || x < minx || y > maxy || y < miny) {
                addErrorRecord("BSM:" + bsm + "的点超出范围！",bsm, ErrorLevelEnum.SERIOUS);
            }
            //间接坐标面构成类型
            readLine();
            //对象的个数
            int ptCount = Integer.valueOf(readLine());
            List<String> bsmList = getRefLines(ptCount);
            checkPolygon(bsm, bsmList);
            //读取结尾的0
            readLine();
            return 1;
        } catch (Exception ex) {
            addErrorRecord("读取"+getVctPartEnum().getDesc()+"中bsm="+bsm+"数据行异常！",bsm, ErrorLevelEnum.SERIOUS);
            systemLog.error(ex);
            if (skipToNextRecord()) {
                return 2;
            }
            return -1;
        }
    }

    /**
     * @return java.util.List<java.lang.Long>
     * @Author Jason
     * @Description 读取面的引用线集合
     * @Date 2022/1/11
     * @Param [ptCount]
     **/
    private List<String> getRefLines(int ptCount) {
        List<String> bsms = new ArrayList<>();
        int bsmCount = 0;
        while (bsmCount < ptCount) {
            String s = readLine();
            String[] spt = s.split("\\,");
            if (spt != null && spt.length > 0) {
                for (int i = 0; i < spt.length; i++) {
                    if (!SPLITZERO.equals(spt[i])) {
                        bsmCount++;
                    }
                    bsms.add(spt[i]);
                }
            } else {
                break;
            }
        }
        return bsms;
    }

    /**
     * @return void
     * @Author Jason
     * @Description 验证当前图形是否正常
     * @Date 2022/1/11
     * @Param [bsm, bsms]
     **/
    private void checkPolygon(String bsm, List<String> bsms) {
        AtomicInteger ptCount = new AtomicInteger();
        bsms.forEach(k -> {
            if(SPLITZERO.equals(k)){
                //数据闭合判断
                if(ptCount.get()<MINPOINTS){
                    addErrorRecord("bsm:" + bsm + "的面数据构造异常，至少需要三个点才能构面！",bsm, ErrorLevelEnum.SERIOUS);
                    return;
                }
                ptCount.set(0);
            }
            else {
                int pts = refLineHelper.getPointCount(k);
                ptCount.addAndGet(pts);
            }
        });
    }


    @Override
    public VctPartEnum getVctPartEnum() {
        return VctPartEnum.Polygon;
    }

    @Override
    public boolean prePare(String file, VctRuleEnum vctRuleEnum, ICheckRecord record){
        if(super.prePare(file,vctRuleEnum,record)){
            if(vctRuleEnum== VctRuleEnum.PRESENT) {
                refLineHelper = new RefLineHelper(file,file);
            }
            else{
                refLineHelper = new RefLineHelper(file,null);
            }
        }
        return false;
    }

    @Override
    public void close(){
        super.close();
        if(refLineHelper!=null){
            refLineHelper.close();
            refLineHelper=null;
        }
    }

}
