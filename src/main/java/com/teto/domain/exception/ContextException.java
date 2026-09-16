package com.teto.domain.exception;

public class ContextException extends RuntimeException{
    private String msg;

    public ContextException(String msg) {
        this.msg = msg;
    }
}
