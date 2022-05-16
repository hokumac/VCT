package com.threeice.vct.filestruct.index;

import com.sun.tools.classfile.LineNumberTable_attribute;
import com.threeice.vct.core.enums.ErrorLevelEnum;
import com.threeice.vct.core.enums.VctPartEnum;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author Jason
 * @Description 地球空间格式通用索引
 * 该索引未明确面的引用线来源，因此需要将所有的线作为引用线
 * 为了避免引用线过多导致的内存问题，实现上先扫描所有面确定引用线的bsm，再根据这些bsm集合初始化引用线，这种处理方式效率上会受到影响，但内存使用上更加友好
 * @Date  2022/1/14
 * @Param
 * @return
 **/
public class VctIndex {

    protected boolean hasReadIdx=false;

    public HashMap<String, String> getRefLine() {
        return refLine;
    }

    //引用线bsm
    protected HashMap<String,String> refLine;

    protected String vctFile;

    protected RandomAccessFile randomAccessFile;

    /**
     * @Author Jason
     * @Description 当前数据行
     * @Date  2022/1/7
     * @Param
     * @return
     **/
    protected long currentRow=0;

    protected String readLine(){
        try {
            if(randomAccessFile==null){
                return null;
            }

            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = randomAccessFile.readLine()) != null) {
                // 显示行号
                currentRow++;
                if(!"".equals(tempString)){
                    return tempString;
                }
            }
            return tempString;
        } catch (IOException e) {
            return null;
        }
    }

    public  boolean loadTopLineIdx(String vctFilePath,String idxFilePath) throws FileNotFoundException {
        if(hasReadIdx){
            return true;
        }
        refLine =new HashMap<>();
        //不依赖索引文件，通过扫描vct文件构造索引数据
        randomAccessFile = new RandomAccessFile(vctFilePath,"r");
        String strLine = readLine();

        //面数据是否读完
        boolean finishPolygon=false;
        //记录线的起始位置
        long lineBegin=0;
        while (strLine != null) {
            if (strLine.equals(VctPartEnum.Polygon + "Begin")) {
                while (readBSM()) {
                    //循环读取所有的引用线表示码
                }
                finishPolygon = true;
            } else if (strLine.equals(VctPartEnum.Polygon + "End")) {
                finishPolygon = true;
            }
            else if(strLine.equals(VctPartEnum.Line+"Begin")){
                try {
                    lineBegin=randomAccessFile.getFilePointer()-strLine.length();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(finishPolygon){
                return readRefLine(lineBegin);
            }
            strLine = readLine();
        }

        return false;
    }

    private boolean readBSM(){
        try {
            //标识码
            String bsm = readLine();
            //要素代码
            readLine();
            //图形表现编码
            readLine();
            //面的特征类型
            readLine();
            //标识点X坐标,标识点Y坐标
            String s = readLine();
            //间接坐标面构成类型
            readLine();
            //对象的个数
            int ptCount = Integer.valueOf(readLine());
            getRefLines(ptCount);
            //读取0
            readLine();
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    private boolean readSingleLine(){
        try {
            //标识码
            String bsm = readLine();
            if(refLine.containsKey(bsm)){
                refLine.put(bsm, String.valueOf(randomAccessFile.getFilePointer()-bsm.length()));
            }
            //要素代码
            readLine();
            //图形表现编码
            readLine();
            //线的特征类型
            readLine();
            //线段条数
            readLine();
            //线段类型
            readLine();
            //点数
            int ptCount = Integer.valueOf(readLine());
            while (ptCount > 0) {
                readLine();
                ptCount--;
            }
            //读取结尾的0
            readLine();
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    protected void getRefLines(int ptCount) {
        int bsmCount = 0;
        while (bsmCount < ptCount) {
            String s = readLine();
            String[] spt = s.split("\\,");
            if (spt != null && spt.length > 0) {
                for (int i = 0; i < spt.length; i++) {
                    String bsm =spt[i];
                    if (!"0".equals(bsm)) {
                        bsmCount++;
                    }
                    if(bsm.charAt(0)=='-'){
                        bsm=bsm.substring(1);
                    }
                    refLine.put(bsm,"");
                }
            } else {
                break;
            }
        }
    }

    protected boolean readRefLine(long lineBigen){
        if(randomAccessFile==null){
            return false;
        }

        try {
            randomAccessFile.seek(lineBigen);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String strLine = readLine();
        while (strLine != null) {
            if (strLine.equals(VctPartEnum.Line + "Begin")) {
                while (readSingleLine()) {
                    //循环读取所有的引用线表示码
                }
                return true;
            } else if (strLine.equals(VctPartEnum.Line + "End")) {
                return true;
            }
            strLine = readLine();
        }
        return false;
    }

    public void close(){
        currentRow=0;
        if(randomAccessFile!=null){
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            randomAccessFile=null;
        }
        if(refLine!=null){
            refLine.clear();
        }
    }


}
