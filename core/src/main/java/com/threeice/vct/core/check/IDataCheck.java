package com.threeice.vct.core.check;
import com.threeice.vct.core.model.CheckResultInfo;

/**
 * @Author Jason
 * @Description vct数据检查
 * @Date  2022/1/6
 * @Param
 * @return
 **/
public interface IDataCheck {
    boolean check(String file,ICheckRecord checkRecord);
}
