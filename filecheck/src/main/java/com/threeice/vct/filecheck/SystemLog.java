package com.threeice.vct.filecheck;

import com.threeice.vct.core.ISystemLog;

public class SystemLog implements ISystemLog {
    @Override
    public void info(String msg) {
        System.out.println(msg);
    }

    @Override
    public void debug(String msg) {
        System.out.println(msg);
    }

    @Override
    public void error(String msg) {
        System.out.println(msg);
    }

    @Override
    public void error(String msg, Exception ex) {
        System.out.println(msg);
        System.out.println(ex);
    }

    @Override
    public void error(Exception ex) {
        System.out.println(ex);
    }
}
