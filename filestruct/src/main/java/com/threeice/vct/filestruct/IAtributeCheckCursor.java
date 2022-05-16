package com.threeice.vct.filestruct;

import com.threeice.vct.filestruct.model.TableStruct;

/**
 * @Author Jason
 * @Description vct数据节点遍历
 * @Date  2022/1/7
 * @Param
 * @return
 **/
public interface IAtributeCheckCursor<T> extends ISegmentCursor<T> {

    int checkAtribute(TableStruct tableStruct);
}
