package com.threeice.vct.filecheck;

import com.threeice.vct.core.IProcess;
import com.threeice.vct.core.ISystemLog;
import com.threeice.vct.core.check.ICheckRecord;
import com.threeice.vct.core.check.IDataCheck;
import com.threeice.vct.core.enums.GeometryTypeEnum;
import com.threeice.vct.core.enums.VctPartEnum;
import com.threeice.vct.core.enums.VctRuleEnum;
import com.threeice.vct.filestruct.IAtributeCheckCursor;
import com.threeice.vct.filestruct.IGeometryCheckCursor;
import com.threeice.vct.filestruct.index.VctIndex;
import com.threeice.vct.filestruct.model.FeatureCode;
import com.threeice.vct.filestruct.model.TableStruct;
import com.threeice.vct.filestruct.refline.RefLineHelper;
import com.threeice.vct.filestruct.segment.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author Jason
 * @Description 国土行业vct数据检查类
 * @Date 2022/1/7
 * @Param
 * @return
 **/
public class PrecentVctFileCheck implements IDataCheck {

    private ISystemLog systemLog;

    private IProcess process;

    public VctRuleEnum getVctRule(){
        return VctRuleEnum.PRESENT;
    }

    public PrecentVctFileCheck(ISystemLog systemLog,IProcess process) {
        this.systemLog = systemLog;
        this.process=process;
    }

    @Override
    public boolean check(String file, ICheckRecord checkRecord) {
        try {
            checkMsg(new Date() + "开始执行数据格式检查！", true);
            //基础检查，extend检查
            HeadPlanSegment head = new HeadPlanSegment(systemLog);
            VctRuleEnum vctRuleEnum = getVctRule();
            if (head.prePare(file, vctRuleEnum, checkRecord)) {
                //要素代码检查
                FeatureCodeSegment featureCodeSegment = new FeatureCodeSegment(systemLog);
                if (featureCodeSegment.prePare(file, vctRuleEnum, checkRecord)) {
                    //结构检查
                    TableStructureSegment tableStructureSegment = new TableStructureSegment(systemLog);
                    if (tableStructureSegment.prePare(file, vctRuleEnum, checkRecord)) {

                        int taskCount = tableStructureSegment.getTableStructs().size() * 2;
                        //并行检查点、线、面、注记、属性
                        ExecutorService exec = new ThreadPoolExecutor(20, taskCount,
                                60L, TimeUnit.SECONDS,
                                new SynchronousQueue<Runnable>());
                        CountDownLatch countDownLatch = new CountDownLatch(taskCount);
                        //检查点线、注记、属性表
                        checkThread(file, vctRuleEnum, checkRecord, head, tableStructureSegment,taskCount,exec,countDownLatch);
                        //检查面数据
                        if(!checkPolygonThread(file, vctRuleEnum, checkRecord, head, tableStructureSegment,taskCount,exec,countDownLatch)){

                        }
                        try {
                            countDownLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    tableStructureSegment.close();
                }
                featureCodeSegment.close();
            }
            head.close();
            checkMsg(new Date() + "数据格式检查完成！", true);
            return false;
        } catch (Exception ex) {
            systemLog.error(ex);
            checkMsg(new Date() + "数据格式检查结束！", true);
            return false;
        }
    }

    private boolean checkThread(String file, VctRuleEnum vctRuleEnum,
                                ICheckRecord checkRecord,
                                HeadPlanSegment head, TableStructureSegment tableStructureSegment,
                                int taskCount,ExecutorService exec,
                                CountDownLatch countDownLatch) {

        tableStructureSegment.getTableStructs().forEach(k -> {
            //图形数据验证
            if(k.getGeometryTypeEnum()!=GeometryTypeEnum.Polygon) {
                Runnable geometryRunnable = () -> {
                    VctSegmentBase vcetSegment = getVctSegment(systemLog, k, false);
                    checkMsg("开始检查" + k.getTableName() + "的数据信息...", false);
                    if (vcetSegment != null) {
                        if (vcetSegment.prePare(file, vctRuleEnum, checkRecord)) {
                            IGeometryCheckCursor cursor = (IGeometryCheckCursor) vcetSegment;
                            VctPartEnum vctpart = getVctpart(k);
                            if (cursor != null && vctpart != VctPartEnum.None && cursor.seekBegin(k.getTableName(), vctpart)) {
                                while (cursor.checkGeometry(head.getXmax(), head.getYmax(), head.getXmin(), head.getYmin()) > 0) {
                                }
                            }
                        }
                        vcetSegment.close();
                    }
                    checkMsg(k.getTableName() + "的数据检查完成", true);
                    checkProcess((int) countDownLatch.getCount(), taskCount);
                    countDownLatch.countDown();
                };
                exec.submit(geometryRunnable);
            }

            //属性表验证
            Runnable atributeRunnable = () -> {
                VctSegmentBase vcetSegment = new AttibuteSegment(systemLog);
                if (vcetSegment != null) {
                    if (vcetSegment.prePare(file, vctRuleEnum, checkRecord)) {

                        IAtributeCheckCursor cursor = (IAtributeCheckCursor) vcetSegment;
                        if (cursor != null && cursor.seekBegin(k.getTableName(), VctPartEnum.Attibute)) {
                            while (cursor.checkAtribute(k) > 0) {

                            }
                        }
                    }
                    vcetSegment.close();
                }
                checkProcess((int) countDownLatch.getCount(), taskCount);
                countDownLatch.countDown();
            };
            exec.submit(atributeRunnable);

        });

        return true;
    }

    private boolean checkPolygonThread(String file, VctRuleEnum vctRuleEnum,
                                       ICheckRecord checkRecord,
                                       HeadPlanSegment head, TableStructureSegment tableStructureSegment,
                                       int taskCount, ExecutorService exec,
                                       CountDownLatch countDownLatch) {

        List<TableStruct> tableStructList=getPolygonTableStruct(tableStructureSegment.getTableStructs(),true);
        //优先处理索引，索引生成后才开始后续动作
        String idxFilePath =file;
        try {
            if(!RefLineHelper.loadIndex(file,idxFilePath)){
                //清理线程任务锁
                tableStructList.forEach(k->{
                    countDownLatch.countDown();
                });
                return false;
            }
        } catch (Exception e) {
            //清理线程任务锁
            tableStructList.forEach(k->{
                countDownLatch.countDown();
            });
            systemLog.error(e);
            return false;
        }


        tableStructList.forEach(k -> {
            //面图形索引初始化
            Runnable indexRunnable = () -> {
                VctSegmentBase vcetSegment = getVctSegment(systemLog, k, true);
                checkMsg("开始检查" + k.getTableName() + "的数据信息...", false);
                if (vcetSegment != null) {
                    if (vcetSegment.prePare(file, vctRuleEnum, checkRecord)) {
                        IGeometryCheckCursor cursor = (IGeometryCheckCursor) vcetSegment;
                        VctPartEnum vctpart = getVctpart(k);
                        if (cursor != null && vctpart != VctPartEnum.None && cursor.seekBegin(k.getTableName(), vctpart)) {
                            while (cursor.checkGeometry(head.getXmax(), head.getYmax(), head.getXmin(), head.getYmin()) > 0) {
                            }
                        }
                    }
                    vcetSegment.close();
                }
                checkMsg(k.getTableName() + "的数据检查完成", true);
                checkProcess((int) countDownLatch.getCount(), taskCount);
                countDownLatch.countDown();
            };
            exec.submit(indexRunnable);
        });
        return true;
    }

    private VctSegmentBase getVctSegment(ISystemLog systemLog, TableStruct tableStruct, Boolean polygonOnly) {
        if (polygonOnly==null||polygonOnly==false) {
            if (tableStruct.getGeometryTypeEnum() == GeometryTypeEnum.Polygon) {
                if(polygonOnly==false){
                    return null;
                }
                return new PolygonSegment(systemLog);
            }
           else if (tableStruct.getGeometryTypeEnum() == GeometryTypeEnum.Point) {
                return new PointSegment(systemLog);
            }
            else if (tableStruct.getGeometryTypeEnum() == GeometryTypeEnum.Line) {
                return new LineSegment(systemLog);
            }
            else if (tableStruct.getGeometryTypeEnum() == GeometryTypeEnum.Annotation) {
                return new AnnotationSegment(systemLog);
            }
        } else if(polygonOnly){
            if (tableStruct.getGeometryTypeEnum() == GeometryTypeEnum.Polygon) {
                return new PolygonSegment(systemLog);
            }
        }
        return null;
    }

    private VctPartEnum getVctpart(TableStruct tableStruct) {
        if (tableStruct.getGeometryTypeEnum() == GeometryTypeEnum.Point) {
            return VctPartEnum.Point;
        }
        if (tableStruct.getGeometryTypeEnum() == GeometryTypeEnum.Line) {
            return VctPartEnum.Line;
        }
        if (tableStruct.getGeometryTypeEnum() == GeometryTypeEnum.Polygon) {
            return VctPartEnum.Polygon;
        }
        if (tableStruct.getGeometryTypeEnum() == GeometryTypeEnum.Annotation) {
            return VctPartEnum.Annotation;
        }
        return VctPartEnum.None;
    }

    private List<TableStruct> getPolygonTableStruct(List<TableStruct> tableStructs,boolean polygonOnly){
        List<TableStruct> tableStructList=new ArrayList<>();
        tableStructs.forEach(k->{
            if(polygonOnly&&k.getGeometryTypeEnum()==GeometryTypeEnum.Polygon){
                tableStructList.add(k);
            }
            else{
                tableStructList.add(k);
            }
        });
        return tableStructList;
    }

    public void checkProcess(int current, int max) {
       if(process!=null){
           process.checkProcess(current,max);
       }
    }

    public void checkMsg(Object sender, String msg) {
        if(process!=null){
            process.checkMsg(sender,msg);
        }
    }

    protected void checkMsg(String msg, boolean entry) {
        checkMsg(this, msg);
        if(entry){
            checkMsg(this, "\r\n");
        }
    }
}
