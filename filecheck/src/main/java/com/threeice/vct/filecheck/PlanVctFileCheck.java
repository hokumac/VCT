package com.threeice.vct.filecheck;

import com.threeice.vct.core.IProcess;
import com.threeice.vct.core.ISystemLog;
import com.threeice.vct.core.check.ICheckRecord;
import com.threeice.vct.core.check.IDataCheck;
import com.threeice.vct.core.enums.GeometryTypeEnum;
import com.threeice.vct.core.enums.VctPartEnum;
import com.threeice.vct.core.enums.VctRuleEnum;
import com.threeice.vct.filestruct.IAtributeCheckCursor;
import com.threeice.vct.filestruct.IGeometryCheckCursor;
import com.threeice.vct.filestruct.model.TableStruct;
import com.threeice.vct.filestruct.refline.RefLineHelper;
import com.threeice.vct.filestruct.segment.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author Jason
 * @Description 国土行业vct数据检查类
 * @Date 2022/1/7
 * @Param
 * @return
 **/
public class PlanVctFileCheck extends PrecentVctFileCheck  {

    @Override
    public VctRuleEnum getVctRule(){
        return VctRuleEnum.PLAN;
    }

    public PlanVctFileCheck(ISystemLog systemLog,IProcess process) {
        super(systemLog,process);
    }
}
