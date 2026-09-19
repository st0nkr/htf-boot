package com.teto.domain.parser.linenum;

import com.teto.IFile;
import com.teto.command.Context;

import java.util.List;
import java.util.Optional;

public class LinEnumParser implements IFile {

    public void parse(Context ctx, String fileName) {
        Optional<List<String>> lines = readFileAsLines(fileName);
        if(lines.isPresent()) {
            for(String line : lines.get()) {
                System.out.println(line);
            }
        }
    }
}
