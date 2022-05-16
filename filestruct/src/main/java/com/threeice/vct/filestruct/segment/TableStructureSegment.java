package com.threeice.vct.filestruct.segment;

import com.threeice.vct.core.ISystemLog;
import com.threeice.vct.core.enums.ErrorLevelEnum;
import com.threeice.vct.core.enums.VctPartEnum;
import com.threeice.vct.filestruct.model.FeatureCode;
import com.threeice.vct.filestruct.model.TableStruct;
import com.threeice.vct.filestruct.model.VctFiled;
import sun.tools.jconsole.Tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableStructureSegment extends VctSegmentBase<TableStructureSegment> {

    public List<TableStruct> getTableStructs() {
        return tableStructs;
    }

    private List<TableStruct> tableStructs;

    public TableStructureSegment(ISystemLog systemLog) {
        super(systemLog);
    }

    @Override
    public VctPartEnum getVctPartEnum() {
        return VctPartEnum.TableStructure;
    }

    @Override
    public boolean initSegment() {
        try {
            tableStructs = new ArrayList<>();
            String first = readLine();
            while (first != null && first != "TableStructureEnd") {
                if (first == "0") {
                    continue;
                }
                String[] split = first.split("\\,");
                int filedCount = Integer.valueOf(split[1]);
                TableStruct tableStruct = new TableStruct(split[0], filedCount);
                while (filedCount > 0) {
                    VctFiled vctFiled=new VctFiled(readLine());
                    tableStruct.getFileds().add(vctFiled);
                    filedCount--;
                }
                tableStructs.add(tableStruct);
            }
            return tableStructs.size()>0;
        } catch (Exception ex) {
            systemLog.error(ex);
            addErrorRecord("读取数据表结构异常！",null, ErrorLevelEnum.DEADLY);
            //如果读取到部分数据也继续后续检查
            return tableStructs.size() > 0;
        }
    }

}