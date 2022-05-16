package com.threeice.vct.filecheck;

import com.threeice.vct.core.check.ICheckRecord;
import com.threeice.vct.core.check.IDataCheck;
import com.threeice.vct.core.enums.VctRuleEnum;
import com.threeice.vct.core.model.CheckResultInfo;

/**
 * @Author Jason
 * @Description vct文件格式检查入口，负责组织检查vct文件类型，检查策略，结果输出定义等
 * @Date  2022/1/6
 * @Param
 * @return
 **/
public class VctCheckManager {
    public CheckResultInfo checkVctFile(String file, VctRuleEnum vctRuleEnum){
        IDataCheck dataCheck=getDataCheck(vctRuleEnum);
        //进度绑定
        //结果写入
        ICheckRecord checkRecord=new CheckRecordLog();
        dataCheck.check(file,checkRecord);
        return null;
    }

    private IDataCheck getDataCheck(VctRuleEnum vctRuleEnum){
        if(vctRuleEnum==VctRuleEnum.PRESENT) {
            //return new PrecentVctFileCheck(new SystemLog());
        }
        return null;
    }
}
