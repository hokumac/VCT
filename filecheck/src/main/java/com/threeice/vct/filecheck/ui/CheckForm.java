package com.threeice.vct.filecheck.ui;

import com.threeice.vct.core.IProcess;
import com.threeice.vct.core.ISystemLog;
import com.threeice.vct.core.check.ICheckRecord;
import com.threeice.vct.core.check.IDataCheck;
import com.threeice.vct.core.enums.VctRuleEnum;
import com.threeice.vct.core.model.CheckErrorInfo;
import com.threeice.vct.core.model.CheckResultInfo;
import com.threeice.vct.filecheck.CheckRecordLog;
import com.threeice.vct.filecheck.PlanVctFileCheck;
import com.threeice.vct.filecheck.SystemLog;
import com.threeice.vct.filecheck.PrecentVctFileCheck;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class CheckForm implements ICheckRecord , IProcess , ISystemLog {
    private JPanel panel1;
    private JButton 选择Button;
    private JTextField 选择vct文件TextField;
    private JTabbedPane tabbedPane1;
    private JRadioButton 土地利用现状RadioButton;
    private JButton 执行检查Button;
    private JRadioButton 土地利用规划RadioButton;
    private JProgressBar progressBar1;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTable table1;
    private RecordTableModel recordTableModel;

    private String[] columnNames = {"Planet", "Radius", "Moons", "Gaseous", "Color"};

    public void initCompenet(JFrame frame){
        recordTableModel=new RecordTableModel();
        table1.setModel(recordTableModel);
        //选择vct文件
        选择Button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //当按钮被点击时，Swing框架会调用监听器的actionPerformed()方法
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.addChoosableFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        if(f.getName().endsWith(".vct")||f.getName().endsWith(".VCT")){
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public String getDescription() {
                        return ".vct";
                    }
                });
                int option = fileChooser.showOpenDialog(frame);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    选择vct文件TextField.setText(file.getPath());
                }
            }
        });

        //执行检查
        执行检查Button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                File file =new File(选择vct文件TextField.getText());
                if(file.exists()) {
                    checkVctFile(选择vct文件TextField.getText());
                }
            }
        });
    }

    public CheckResultInfo checkVctFile(String file){

        VctRuleEnum vctRuleEnum = VctRuleEnum.PLAN;
        if(土地利用现状RadioButton.isSelected()){
            vctRuleEnum = VctRuleEnum.PRESENT;
        }
        IDataCheck dataCheck=getDataCheck(vctRuleEnum);
        //进度绑定

        //结果写入
        dataCheck.check(file,this);

        table1.updateUI();
        return null;
    }

    private IDataCheck getDataCheck(VctRuleEnum vctRuleEnum){
        if(vctRuleEnum==VctRuleEnum.PRESENT) {
            return new PrecentVctFileCheck(this,this);
        }
        return new PlanVctFileCheck(this,this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("VCT数据格式检查工具");
        CheckForm frm=  new CheckForm();
        frm.initCompenet(frame);
        frame.setContentPane(frm.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public boolean addErrorRecord(CheckErrorInfo errorInfoList) {
        recordTableModel.getDatas().add(errorInfoList);
        return true;
    }

    @Override
    public void flush() {

    }

    @Override
    public void checkProcess(int current, int max) {
        progressBar1.setMaximum(max);
        progressBar1.setValue(current);
    }

    @Override
    public void checkMsg(Object sender, String msg) {
        textArea1.setText(textArea1.getText()+msg);
    }

    @Override
    public void info(String msg) {
        textArea2.setText(textArea2.getText()+msg);
    }

    @Override
    public void debug(String msg) {
        textArea2.setText(textArea2.getText()+msg);
    }

    @Override
    public void error(String msg) {
        textArea2.setText(textArea2.getText()+msg);
    }

    @Override
    public void error(String msg, Exception ex) {
        textArea2.setText(textArea2.getText()+msg);
        if(ex!=null) {
            textArea2.setText(textArea2.getText() + ex.getMessage()+ex.getStackTrace());
        }
    }

    @Override
    public void error(Exception ex) {
        if(ex!=null) {
            textArea2.setText(textArea2.getText() + ex.getMessage()+ex.getStackTrace());
        }
    }
}
