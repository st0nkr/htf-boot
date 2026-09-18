package com.teto.domain.parser.hydra;

import com.teto.IFile;
import com.teto.IJSON;
import com.teto.command.Context;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;

import java.util.LinkedHashMap;
import java.util.Optional;

public class HydraParser implements IFile , IJSON {
    public ScannedTargets parse(Context ctx, Target target, String fileName) {
        final ScannedTargets st = new ScannedTargets();
        Optional<String> contents = readFile(fileName);
        if(contents.isPresent()) {
            Optional<LinkedHashMap> obj = fromJson(contents.get(), LinkedHashMap.class);
            if(obj.isPresent()) {
                System.out.println("Here");
            }
        }
        return st;
    }
}
