package com.threeice.vct.filecheck.ui;

import com.threeice.vct.core.enums.ErrorLevelEnum;
import com.threeice.vct.core.enums.VctPartEnum;
import com.threeice.vct.core.model.CheckErrorInfo;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RecordTableModel extends AbstractTableModel {


    String[] columnNames = new String[]{"id", "错误级别", "行数", "BSM", "数据段", "错误描述", "错误行"};

    public List<CheckErrorInfo> getDatas() {
        return datas;
    }

    private List<CheckErrorInfo> datas = new ArrayList<>();

    @Override
    public int getRowCount() {
        return datas.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        // TODO Auto-generated method stub
        return columnNames[columnIndex];

    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || columnIndex < 0 || columnIndex >= columnNames.length || rowIndex >= datas.size()) {
            return null;
        }

        CheckErrorInfo checkErrorInfo = datas.get(rowIndex);
        if (checkErrorInfo == null) {
            return null;
        }
        if (columnIndex == 0) {
            if(checkErrorInfo.getId()!=null){
                return checkErrorInfo.getId();
            }
            else{
                return rowIndex+1;
            }
        }
        else if (columnIndex == 1) {
            return checkErrorInfo.getErrorLevelEnum();
        }
        else if (columnIndex == 2) {
            return checkErrorInfo.getRownum();
        }
        else if (columnIndex == 3) {
            return checkErrorInfo.getBsm();
        }
        else if (columnIndex == 4) {
            return checkErrorInfo.getVctPartEnum();
        }
        else if (columnIndex == 5) {
            return checkErrorInfo.getErrorMsg();
        }
        else if (columnIndex == 6) {
            return checkErrorInfo.getErrorVctLine();
        }
        return null;
    }
}
