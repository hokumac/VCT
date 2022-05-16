package com.threeice.vct.core;

/**
 * @Author Jason
 * @Description 检查进度反馈接口
 * @Date  2022/1/6
 * @Param
 * @return
 **/
public interface IProcess {
    void checkProcess(int current,int max);

    void checkMsg(Object sender,String msg);
}
