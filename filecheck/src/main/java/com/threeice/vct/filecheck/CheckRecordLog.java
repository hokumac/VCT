package com.threeice.vct.filecheck;

import com.threeice.vct.core.check.ICheckRecord;
import com.threeice.vct.core.model.CheckErrorInfo;

import java.util.List;

public class CheckRecordLog implements ICheckRecord {
    @Override
    public boolean addErrorRecord(CheckErrorInfo errorInfoList) {
        if(errorInfoList!=null){
                System.out.println(errorInfoList);
        }
        return true;
    }

    @Override
    public void flush() {

    }
}
