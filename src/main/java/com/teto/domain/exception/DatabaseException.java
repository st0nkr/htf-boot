package com.teto.domain.exception;

public class DatabaseException extends Exception{
    private final String msg;

    public DatabaseException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
