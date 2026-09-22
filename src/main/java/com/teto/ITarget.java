package com.teto;

import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;
import com.teto.domain.target.TargetTypeMapper;
import us.springett.parsers.cpe.Cpe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface ITarget extends IProperties, ICSV, ICPE, IIPAddresses {

    default boolean isWindows(Context ctx, Target os) {
        if(os.getOsFamily() != null && os.getOsFamily().equalsIgnoreCase("Windows")) {
            return true;
        }
        return false;
    }

    default boolean isLinux(Context ctx, Target os) {
        if(os.getOsFamily() != null && os.getOsFamily().equalsIgnoreCase("Linux")) {
            return true;
        }
        if(os.getOsVersion() != null && os.getOsVersion().contains("Linux")) {
            return true;
        }
        if(os.getCpe() != null) {
            Cpe cpe = parseCPE(ctx, os.getCpe());

        }
        return false;
    }

    default boolean isIPV4(String ip) {
        return isValidIPV4(ip);
    }

    default boolean isIPV6(String ip) {
        return isValidIPV6(ip);
    }
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

    default Target getTarget(Context ctx, TargetNode node, TargetType tt) {
        for(Target target : node.getScannedTargets().getTargets()) {
            TargetType tt1 = TargetType.fromString(target.getTargetType());
            if(tt.equals(tt1)) {
                return target;
            }
        }
        return null;
    }
    default Target getTarget(Context ctx, TargetNode node, String serviceName) {
        for(Target target : node.getScannedTargets().getTargets()) {
            if(TargetType.Service.equals(getTargetType(ctx, target))) {
                if(serviceName.equalsIgnoreCase(target.getName())) {
                    return target;
                }
            }
        }
        return null;
    }
}
