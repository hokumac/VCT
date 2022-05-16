package com.threeice.vct.filestruct.refline;


import com.threeice.vct.filestruct.index.PresentVctIndex;
import com.threeice.vct.filestruct.index.VctIndex;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

/**
 * @Author Jason
 * @Description 引用线数据读取辅助类
 * @Date 2022/1/11
 * @Param
 * @return
 **/
public class RefLineHelper {

    private static boolean hasLoadIndex = false;


    private static HashMap<String, String> refLine;

    protected RandomAccessFile randomAccessFile;

    /**
     * @Author Jason
     * @Description 当前数据行
     * @Date 2022/1/7
     * @Param
     * @return
     **/
    protected long currentRow = 0;

    protected String readLine() {
        try {
            if (!hasLoadIndex || randomAccessFile == null) {
                return null;
            }

            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = randomAccessFile.readLine()) != null) {
                // 显示行号
                currentRow++;
                if (!"".equals(tempString)) {
                    return tempString;
                }
            }
            return tempString;
        } catch (IOException e) {
            return null;
        }
    }

    public RefLineHelper(String vctFile, String vctIdxFile) {
        //bufferedReader.skip()
        //bufferedReader.seek();
    }

    public String readPoints(Long bsm) {
        return "";
    }

    public int getPointCount(String bsm) {
        if (hasLoadIndex) {
            return -1;
        }

        Long offset = null;
        if (refLine.containsKey(bsm)) {
            offset = Long.valueOf(refLine.get(bsm));
        } else {
            return -1;
        }

        //设置到引用线起始位置
        try {
            randomAccessFile.seek(offset);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //标识码
        readLine();
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
        return Integer.valueOf(readLine());
    }

    public static boolean loadIndex(String vctFile, String vctIdxFile) {
        VctIndex vctIndex =null;
        if(vctIdxFile==null||vctIdxFile.length()==0){
            //使用通用索引
             vctIndex =new VctIndex();
        }
        else{
            //使用土地利用现状索引
            vctIndex =new PresentVctIndex();
        }
        try {
            if(vctIndex.loadTopLineIdx(vctFile,vctIdxFile)){
                refLine =vctIndex.getRefLine();
                return true;
            }
        } catch (FileNotFoundException e) {
            System.out.print(e);
        }
        return false;
    }

    public void close(){
        if(randomAccessFile!=null){
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            randomAccessFile=null;
        }
    }
}
