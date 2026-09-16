package com.teto;

public interface CSVVisitor {
    void handle(int lc, String[] line);

    void handle(int lc, Exception e);
}
