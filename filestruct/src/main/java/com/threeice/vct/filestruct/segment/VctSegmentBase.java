package com.threeice.vct.filestruct.segment;

import com.threeice.vct.core.ISystemLog;
import com.threeice.vct.core.check.ICheckRecord;
import com.threeice.vct.core.enums.ErrorLevelEnum;
import com.threeice.vct.core.enums.VctPartEnum;
import com.threeice.vct.core.enums.VctRuleEnum;
import com.threeice.vct.core.model.CheckErrorInfo;

import java.io.*;


public abstract class VctSegmentBase<T> {

    public VctSegmentBase(ISystemLog systemLog){
        this.systemLog=systemLog;
    }

    public abstract VctPartEnum getVctPartEnum();

    public  ErrorLevelEnum getErrorLevelEnum(){
        return ErrorLevelEnum.DEADLY;
    }

    /**
     * @Author Jason
     * @Description vct格式检查错误记录类
     * @Date  2022/1/7
     * @Param
     * @return
     **/
    protected ICheckRecord checkRecord;


    /**
     * @Author Jason
     * @Description 系统日志管理接口
     * @Date  2022/1/10
     * @Param
     * @return
     **/
    protected ISystemLog systemLog;

    protected String vctLine="";

    protected LineNumberReader bufferedReader;




    protected String readLine(){
        try {
            if(bufferedReader==null){
                return null;
            }
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = bufferedReader.readLine()) != null) {
                if(!"".equals(tempString)){
                    vctLine=tempString;
                    return tempString;
                }
            }
            return tempString;
        } catch (IOException e) {
            addErrorRecord("读取"+getVctPartEnum().getDesc()+"的数据行异常！",null,ErrorLevelEnum.DEADLY);
           return null;
        }
    }

    /**
     * @Author Jason
     * @Description 跳转到下一个记录
     * @Date  2022/1/11
     * @Param []
     * @return boolean
     **/
    protected boolean skipToNextRecord(){
        try {
            String tempString = null;
            while((tempString = readLine()) != null){
                if("0".equals(tempString)){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            addErrorRecord("跳转记录"+getVctPartEnum().getDesc()+"的数据行异常！",null,ErrorLevelEnum.DEADLY);
            return false;
        }
    }

    protected  boolean initSegment(){
        String tempString = null;
        while((tempString = readLine()) != null){
            if(tempString.equals(getVctPartEnum().getCode()+"Begin")){
                return true;
            }
        }
        addErrorRecord("未能找到"+getVctPartEnum().getDesc()+"的起始位置！",null,getErrorLevelEnum());
        return false;
    }

    protected void addErrorRecord(String msg, String bsm,ErrorLevelEnum errorLevelEnum){
        if(checkRecord!=null){
            CheckErrorInfo checkInfo =new CheckErrorInfo();
            if(bufferedReader!=null){
                checkInfo.setRownum(Long.valueOf(bufferedReader.getLineNumber()));
            }
            else {
                checkInfo.setRownum(0L);
            }
            if(bsm!=null) {
                checkInfo.setBsm(bsm);
            }
            checkInfo.setVctPartEnum(getVctPartEnum());
            checkInfo.setErrorVctLine(vctLine);
            checkInfo.setErrorMsg(msg);
            checkInfo.setErrorLevelEnum(errorLevelEnum);
            checkRecord.addErrorRecord(checkInfo);
        }
    }


    public boolean seekVctPartBegin(String file) {
        try {
            File vctFile = new File(file);
            bufferedReader = new LineNumberReader(new FileReader(vctFile));
            String strLine = readLine();
            while (strLine != null) {
                if (strLine.equals(getVctPartEnum().getCode() + "Begin")) {
                    return true;
                }
                strLine = readLine();
            }
            addErrorRecord("未能找到"+getVctPartEnum().getDesc()+"的起始位置！",null,getErrorLevelEnum());
            return false;
        } catch (Exception ex) {
            addErrorRecord("读取"+getVctPartEnum().getDesc()+"的起始位置异常！",null,ErrorLevelEnum.DEADLY);
            systemLog.error(ex);
            return false;
        } finally {
        }
    }

    public boolean prePare(String file,VctRuleEnum vctRuleEnum,ICheckRecord record){
        this.checkRecord=record;
        if(seekVctPartBegin(file)) {
            if( initSegment()){
                return true;
            }
        }
        return false;
    }

   public void close(){
        vctLine="";
        if(checkRecord!=null){
            checkRecord.flush();
        }
       if(bufferedReader!=null){
           try {
               bufferedReader.close();
               bufferedReader=null;
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   }

}
