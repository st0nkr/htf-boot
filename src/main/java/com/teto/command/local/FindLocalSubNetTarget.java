package com.teto.command.local;

import com.teto.*;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommand;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;

public class FindLocalSubNetTarget extends AbstractCommand<Target> implements IJSON, IMerge, ITarget, INetwork, IDuration {
    @Override
    public Optional<Target> apply(Context ctx) {
        String cmd = "ip -j address";
        Optional<RunCommandResponse> rsp = ctx.apply(new RunCommand(cmd, 0, seconds(30)));
        Optional<ArrayList> json = fromJson(rsp.get().getOutput(), ArrayList.class);
        for(int idx = 0; idx < json.get().size(); idx++) {
            LinkedHashMap lhm = (LinkedHashMap) json.get().get(idx);
            String ifName = (String) lhm.get("ifname");
            switch(ifName.toLowerCase()) {
                case "eth0": return optional(createTarget(ctx,lhm));
            }
        }
        return Optional.empty();
    }

    private Target createTarget(Context ctx, LinkedHashMap lhm) {
        Target t = new Target();
        t.setTargetType(TargetType.LocalMe.name());
        t.setIpAddress(myIPAddress(ctx).getIp());
        t.setUri(t.getIpAddress());
        t.setName(lhm.get("ifname").toString());
        t.setIface(t.getName());
        ArrayList addrs = (ArrayList) lhm.get("addr_info");
        for(int idx = 0 ; idx < addrs.size(); idx++) {
            LinkedHashMap addr = (LinkedHashMap) addrs.get(idx);

            String family = addr.get("family").toString();
            if("inet".equalsIgnoreCase(family)) {
                String local = addr.get("local").toString();
                Integer prefixLen = Integer.parseInt(addr.get("prefixlen").toString());
                t.setSubNetMask(t.getIpAddress() + "/" + prefixLen);
                if(!local.equals(t.getIpAddress())) {
                    Target merged = merge(new Target(), t);
                    merged.setIpAddress(local);
                    merged.setUri(local);
                    if(isIPV4(local)) {
                        merged.setTargetType(TargetType.Ipv4.name());
                        merged.setSubNetMask(local + "/" + prefixLen);
                    }
                    if(isIPV6(local)) {
                        merged.setTargetType(TargetType.Ipv6.name());
                    }

                    if(t.getShared() == null) {
                        t.setShared(new ArrayList<>());
                    }
                    t.getShared().add(merged);
                }


            }
        }
        return t;
    }
}
