package com.teto;

import com.teto.command.Context;
import com.teto.domain.ipaddress.IPAddress;
import com.teto.domain.local.TargetNode;
import com.teto.domain.target.TargetType;

public interface ITargetNode extends ITarget, INetwork{
    default boolean isA(Context ctx, TargetType tt, TargetNode node) {
        TargetType tt2 = getTargetType(ctx, node.getTarget());
        return tt.equals(tt2);
    }

    default boolean isAVirtualBox(Context ctx, TargetNode node) {
        return isA(ctx, TargetType.VirtualBox, node);
    }

    default boolean isMe(Context ctx, TargetNode node) {
        IPAddress me = myIPAddress(ctx);
        String ip1 = me.getIp();
        String ip2 = node.getTarget().getIpAddress();
        return ip1.equalsIgnoreCase(ip2);
    }
}
