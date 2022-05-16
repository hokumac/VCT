package com.threeice.vct.filestruct.segment;

import com.threeice.vct.core.ISystemLog;
import com.threeice.vct.core.enums.ErrorLevelEnum;
import com.threeice.vct.core.enums.VctPartEnum;
import com.threeice.vct.filestruct.IAtributeCheckCursor;
import com.threeice.vct.filestruct.model.TableStruct;

public class AttibuteSegment  extends VctSegmentBase<AttibuteSegment> implements IAtributeCheckCursor<AttibuteSegment> {


    public AttibuteSegment(ISystemLog systemLog) {
        super(systemLog);
    }

    /**
     * @Author Jason
     * @Description //TODO
     * @Date  2022/1/15
     * @Param [tableStruct]
     * @return int 1 正常结束 -1 执行异常 2存在异常但跳过执行
     **/
    @Override
    public int checkAtribute(TableStruct tableStruct) {
        String bsm=null;
        try {
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = readLine()) != null) {
                if(tempString=="TableEnd"){
                    return 1;
                }
               //字段个数
               String [] vals =tempString.split("\\,");
               if(vals==null||vals.length==0){
                   addErrorRecord("读取" + getVctPartEnum().getDesc() + "的数据行异常！",bsm, ErrorLevelEnum.SERIOUS);
                   return 2;
               }
               if((vals.length-1)!=tableStruct.getFiledCount()){
                   addErrorRecord("读取" + getVctPartEnum().getDesc() + "的数据行异常！",bsm, ErrorLevelEnum.SERIOUS);
                   return 1;
               }

                for (int i = 1; i < vals.length; i++) {
                    String msg =tableStruct.getFileds().get(i-1).checkField(vals[i]);
                    if(msg!=null){
                        addErrorRecord(msg,bsm, ErrorLevelEnum.SERIOUS);
                    }
                }
            }
            return 1;
        }catch (Exception ex){
            addErrorRecord("读取" + getVctPartEnum().getDesc() + "的数据行异常！",bsm, ErrorLevelEnum.SERIOUS);
            systemLog.error(ex);
            return -1;
        }
    }


    @Override
    public boolean seekBegin(String tablename, VctPartEnum vctPartEnum) {
        String tempString = null;
        // 一次读入一行，直到读入null为文件结束
        while ((tempString = readLine()) != null) {
            if (tablename.equals(tempString)) {
                return true;
            }

            if(tempString.equals(getVctPartEnum().getCode()+"End")){
                break;
            }
        }
        return false;
    }

    @Override
    public VctPartEnum getVctPartEnum() {
        return VctPartEnum.Attibute;
    }

}
