package com.teto.domain.process;

@FunctionalInterface
public interface IProcessCallBack {
    void handle(String str);
}
