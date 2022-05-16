package com.threeice.vct.filestruct.model;

import lombok.Data;

@Data
public class VctFiled {
    private String filedName;
    private String filedType;
    private int length;
    private int precision;

    public VctFiled(String filedLine){
        String[] split = filedLine.split("\\,");
        filedName =split[0];
        filedType = split[1];
        if(split.length>2){
            length=Integer.valueOf(split[2]);
        }
        if(split.length>3){
            precision=Integer.valueOf(split[3]);
        }
    }

    public String checkField(String val){
return "";
    }
}
