package com.teto;

import com.teto.command.Context;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;
import com.teto.domain.target.TargetTypeMapper;

import java.util.Arrays;
import java.util.List;

public interface ITarget extends IProperties, ICSV {
    default boolean isIPV4(Target t) {
        TargetType tt = TargetType.fromString(t.getTargetType());
        return TargetType.Ipv4.equals(tt);
    }

    default boolean isIPV6(Target t) {
        TargetType tt = TargetType.fromString(t.getTargetType());
        return TargetType.Ipv6.equals(tt);
    }

    default TargetTypeMapper getTargetTypeMapper(Context ctx) {
        TargetTypeMapper ttm = ctx.fetch(TargetTypeMapper.class);
        if(ttm == null) {
            ttm = new TargetTypeMapper();
            String fileName = property(ctx, Tag.TargetTypeMappingFile);
            TargetTypeMapper finalTtm = ttm;
            loadCSVFile(fileName, new CSVVisitor() {
                @Override
                public void handle(int lc, String[] line) {
                    TargetType tt = TargetType.fromString(line[0]);
                    if(tt != null) {
                        List<String> list = Arrays.asList(line[1].split(","));
                        finalTtm.add(tt, list);
                    }
                }

                @Override
                public void handle(int lc, Exception e) {

                }
            });
            ctx.stash(ttm);
        }
        return ttm;
    }
    default TargetType getTargetType(Context ctx, String text) {
        return getTargetTypeMapper(ctx).targetTypeFor(text);
    }

    default TargetType getTargetType(Context ctx, Target t) {
        TargetType tt = TargetType.fromString(t.getTargetType());
        if(tt != null) {
            return tt;
        }
        if(t.getName() != null) {
            return getTargetTypeMapper(ctx).targetTypeFor(t.getName());
        }
        return null;
    }

}
