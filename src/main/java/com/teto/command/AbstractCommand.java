package com.teto.command;


import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractCommand<X>
        implements Command<X>  {
    public String getOSName() {
        return SystemUtils.OS_NAME;
    }
    public boolean isWindows() {
        return getOSName().toLowerCase().contains("windows");
    }

    public Logger logger(Object obj) {
        return LoggerFactory.getLogger(obj.getClass());
    }
    public void info(Object src, String msg) {
        logger(src).info(msg);
    }
    public void warn(Object src, String msg) {
        logger(src).warn(msg);
    }
    public void error(Object src, String msg) {
        logger(src).error(msg);
    }
    public void error(Object src, String msg, Exception e) {
        logger(src).error(msg, e);
    }
}
