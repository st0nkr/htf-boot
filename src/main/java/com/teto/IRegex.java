package com.teto;

import com.teto.command.Context;
import com.teto.command.regex.LoadRegexPatterns;
import com.teto.domain.meta.Tag;
import com.teto.domain.regex.Regex;
import com.teto.domain.target.TargetType;
import org.apache.commons.collections4.properties.SortedProperties;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface IRegex extends IFile, IRegexPatterns{

    default List<Pattern> patterns(String...strings) {
        return Arrays.stream(strings).map(Pattern::compile).toList();
    }
    default boolean isHost(String host) {
        Pattern pattern = Pattern.compile(domain, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(host);
        return matcher.matches();
    }

    default Properties getRegexes(Context ctx) {
        Properties props = ctx.fetch(Tag.Regexes);
        if(props == null) {
            Optional<SortedProperties> sprops = ctx.apply(new LoadRegexPatterns());
            if(ctx.isPresent(sprops)) {
                ctx.stash(Tag.Regexes, sprops.get());
                props = sprops.get();
            }
        }
        return props;
    }


    default List<Pattern> getTargetTypePatterns(Context ctx, TargetType tt) {
        String loc = property(ctx, Tag.TargetTypeRegexDirectory)+ File.separator+tt.name();
        Set<String> files = listFilesUsingFileWalk(loc, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isFile() && pathname.getName().endsWith(".regex")) {
                    return true;
                }
                return false;
            }
        });
        final List<Pattern> patterns = new ArrayList<>();
        for(String file : files) {
            Collection<String> lines = readFileAsUniqueLines(file);
            for(String line : lines) {
                try {
                    Pattern p = Pattern.compile(line, Pattern.CASE_INSENSITIVE);
                    patterns.add(p);
                } catch(Exception e) {

                }
            }
        }
        return patterns;
    }
}
