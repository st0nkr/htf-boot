package com.teto;

import com.teto.command.Context;
import com.teto.domain.cpe.CPE;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.Target;
import us.springett.parsers.cpe.Cpe;
import us.springett.parsers.cpe.CpeBuilder;
import us.springett.parsers.cpe.CpeParser;
import us.springett.parsers.cpe.exceptions.CpeValidationException;
import us.springett.parsers.cpe.values.Part;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface ICPE {

    default boolean isValidCPE(Context ctx, CPE cpe) {
        return true;
    }


    default Cpe parseCPE(Context ctx, String str) {
        try {
            if(str == null) {
                return null;
            }
            Cpe cpe = CpeParser.parse(str);
            return cpe;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    default void append(StringBuilder sb, String value) {
        if("*".equals(value)) {
            return;
        }
        if(value == null || value.isEmpty()) {
            sb.append(value).append(" ");
        }
    }

    default String cpeString(Context  ctx, Target t) throws CpeValidationException {
        CpeBuilder builder = new CpeBuilder();
        Cpe cpe = builder.part(Part.APPLICATION)
                .vendor("apache")
                .version(t.getVersion())
                .product(t.getName().replace(" ","_"))
                .swEdition(t.getOsFamily().replace(" ","_"))
                .targetHw("*")
                .targetSw(t.getOsFamily().replace(" ","_"))
                .build();
        return cpe.toCpe23FS();
    }
    default CPE cpeFromTarget(Context ctx, Target t) {
        CPE s = new CPE(t.getName(), t.getParentId(), t.getLevel());
        s.setProvenance(t.getProvenance());
        s.setUri(t.getUri());
        s.setParentType(t.getParentType());
        s.setOsFamily(t.getOsFamily());
        s.setVersion(t.getVersion());
        s.setTargetType(t.getTargetType());
        return s;
    }

    default String getCpeKeyWords(Context ctx, Target target) {
        StringBuilder sb = new StringBuilder();
        if(target.getCpe() == null) {
            return sb.toString();
        }
        Cpe cpe = parseCPE(ctx, target.getUnderlyingCpe());
        if(cpe == null) {
            return sb.toString();
        }
        append(sb,cpe.getLanguage());
        append(sb,cpe.getEdition());
        append(sb,cpe.getOther());
        switch(cpe.getPart()) {
            case OPERATING_SYSTEM -> sb.append("Operating System ");
            case APPLICATION -> sb.append("Application ");
            case HARDWARE_DEVICE -> sb.append("Device ");
        }
        append(sb,cpe.getProduct());
        append(sb,cpe.getSwEdition());
        append(sb,cpe.getTargetHw());
        append(sb,cpe.getTargetSw());
        append(sb,cpe.getVendor());
        append(sb,cpe.getVersion());
        String[] words = sb.toString().toLowerCase().split(" ");
        Set<String> wordsSet = new HashSet<>(Arrays.asList(words));
        final StringBuilder sb1 = new StringBuilder();
        wordsSet.forEach(word -> {
            sb1.append(word).append(" ");
        });
        return sb1.toString();
    }
    default String getCpeVersion(Context ctx, Target target) {
        if(target.getCpe() == null) {
            return null;
        }
        Cpe cpe = parseCPE(ctx, target.getCpe());
        if(cpe == null) {
            return null;
        }

        return cpe.getVersion();
    }
}
