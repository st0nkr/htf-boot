package com.teto.domain.parser.whatweb;

import com.teto.IFile;
import com.teto.IJSON;
import com.teto.command.Context;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;

import java.util.Optional;


public class WhatWebParser implements IFile, IJSON {

    public ScannedTargets parse(Context ctx, Target http, String fileName) {
        Optional<String> contents = readFile(fileName);
        if(!isPresent(contents)) {
            return null;
        }
        String str = contents.get();
        ScannedTargets st = new ScannedTargets();
        return st;
    }
}
