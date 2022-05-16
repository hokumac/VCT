package com.threeice.vct.filestruct;

/**
 * @Author Jason
 * @Description vct数据节点遍历
 * @Date  2022/1/7
 * @Param
 * @return
 **/
public interface IGeometryCheckCursor<T> extends ISegmentCursor<T> {

    int checkGeometry(double maxx,double maxy,double minx,double miny);
}
