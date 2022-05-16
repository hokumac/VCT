package com.threeice.vct.core.check;

import com.threeice.vct.core.model.CheckErrorInfo;

import java.util.List;

/**
 * @Author Jason
 * @Description 数据检查结果数据访问接口
 * @Date  2022/1/6
 * @Param
 * @return
 **/
public interface ICheckRecord {
    boolean addErrorRecord(CheckErrorInfo errorInfoList);

    void flush();
}
