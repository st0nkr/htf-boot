package com.teto.command.local;

import com.teto.IDuration;
import com.teto.IJSON;
import com.teto.INetwork;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommand;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;

public class FindLocalSubNetTarget extends AbstractCommand<Target> implements IJSON, INetwork, IDuration {
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
                Integer prefixLen = Integer.parseInt(addr.get("prefixlen").toString());
                t.setSubNetMask(t.getIpAddress() + "/" + prefixLen);
            }
        }
        return t;
    }
}
