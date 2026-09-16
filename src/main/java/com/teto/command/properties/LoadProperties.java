package com.teto.command.properties;

import com.teto.IMeta;
import com.teto.IStream;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.meta.Tag;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class LoadProperties extends AbstractCommand<Properties> implements IMeta, IStream {
    private final List<String> args;

    public LoadProperties(List<String> args) {
        this.args = args;
    }

    @Override
    public Optional<Properties> apply(Context ctx) {
        Properties props = ctx.fetch(Tag.Properties);
        if(props != null) {
            return optional(props);
        }
        for(String arg : args) {
            if(arg.startsWith("-"+Tag.ConfigFileName.name()+"=")) {
                String[] parts = arg.split("=");
                if (parts.length == 2) {
                    if (isMeta(Tag.ConfigFileName, parts[0].substring(1).trim())) {
                        String fileName = parts[1].trim();
                        props = new Properties();
                        try {
                            props.load(fileInputStream(fileName));
                            ctx.stash(Tag.Properties.name(), props);
                            ctx.apply(new ApplyCliOverrides(args));
                            return optional(props);
                        } catch (IOException e) {

                        }
                    }

                }
            }
        }
        return Optional.empty();
    }
}
