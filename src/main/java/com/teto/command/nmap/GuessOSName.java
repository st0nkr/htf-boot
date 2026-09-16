package com.teto.command.nmap;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.nmap.Host;
import com.teto.domain.nmap.NmapRun;
import com.teto.domain.nmap.OSClass;
import com.teto.domain.nmap.OSMatch;
import com.teto.domain.parser.nmap.INMapUtils;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;
import us.springett.parsers.cpe.Cpe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuessOSName extends AbstractCommand<String> implements INMapUtils {
    private final Host host;

    public GuessOSName(Host host) {
        this.host = host;
    }

    protected OSMatch getBestMatch(List<OSMatch> matches) {
        if(matches == null || matches.isEmpty()) {
            return null;
        }
        OSMatch os = null;
        for(OSMatch match : matches) {
            if (os == null) {
                os = match;
            } else {
                if (match.getAccuracy() > os.getAccuracy()) {
                    os = match;
                }
            }
        }
        return os;
    }
    @Override
    public Optional<String> apply(Context ctx) {
        Target osTarget = getOperatingSystem(ctx, host);

        OSMatch os = getBestMatch(getOSMatches(ctx, host));
        if(os != null) {
            List<OSClass> clazzes = getOtherOSClasses(osTarget.getCpe(), os.getOsClasses());
            if(clazzes.size() == 1) {
                OSClass clazz = clazzes.get(0);
                if(clazz.getVendor().equals("Slirp") && clazz.getAccuracy() > 90) {
                    return optional(TargetType.Linux.name());
                }
            }
            return optional(os.getName());
        }
        return null;
    }

    private List<OSClass> getOtherOSClasses(String cpeStr, List<OSClass> osClasses) {
        final List<OSClass> clazzes = new ArrayList<>();
        try {
            osClasses.forEach(osClass -> {
                List<Cpe> cpes = osClass.getCpes();
                boolean found = false;
                for(Cpe c : cpes) {
                    String str = c.toString();
                    if(str.equals(cpeStr)) {
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    clazzes.add(osClass);
                }
            });

        } catch (Exception e) {

        }
        return clazzes;
    }


}
