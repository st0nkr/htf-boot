package com.teto.domain.exception;

public class PendingCompletionException extends RuntimeException{
    public PendingCompletionException(String msg) {
        super(msg);
    }
}
