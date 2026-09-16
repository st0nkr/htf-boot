package com.teto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ILogger {
    default Logger logger(Object obj) {
        return LoggerFactory.getLogger(obj.getClass());
    }

    default void info(Object src, String msg) {
        logger(src).info(msg);
    }

    default void warn(Object src, String msg) {
        logger(src).warn(msg);
    }
    default void error(Object src, String msg) {
        logger(src).error(msg);
    }
    default void error(Object src, String msg, Exception e) {
        logger(src).error(msg, e);
    }
}
