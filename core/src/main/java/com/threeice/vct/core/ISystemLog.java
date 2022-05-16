package com.threeice.vct.core;

public interface ISystemLog {
    void info(String msg);
    void debug(String msg);
    void error(String msg);
    void error(String msg,Exception ex);
    void error(Exception ex);
}
