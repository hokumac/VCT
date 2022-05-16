package com.threeice.vct.filestruct;

import com.threeice.vct.core.enums.VctPartEnum;

/**
 * @Author Jason
 * @Description vct数据节点遍历
 * @Date  2022/1/7
 * @Param
 * @return
 **/
public interface ISegmentCursor<T> {

    boolean seekBegin(String tablename, VctPartEnum vctPartEnum);

    void close();

}
