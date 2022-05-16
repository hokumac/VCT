package com.threeice.vct.core.model;

import com.threeice.vct.core.enums.ErrorLevelEnum;
import com.threeice.vct.core.enums.VctPartEnum;
import lombok.Data;

/**
 * @Author Jason
 * @Description vct数据检查错误明细
 * @Date  2022/1/6
 * @Param
 * @return
 **/
@Data
public class CheckErrorInfo {
   private Long id;
   private ErrorLevelEnum errorLevelEnum;
   private Long rownum;
   private String bsm;
   private VctPartEnum vctPartEnum;
   private String errorMsg;
   private String ErrorVctLine;
}
