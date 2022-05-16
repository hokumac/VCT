package com.threeice.vct.core.model;

import lombok.Data;

/**
 * @Author Jason
 * @Description 单次vct文件检查的结果汇总
 * @Date  2022/1/6
 * @Param
 * @return
 **/
@Data
public class CheckResultInfo {
    private Integer errorCount;
    private Long id;
    private boolean success;
    private String msg;
}
