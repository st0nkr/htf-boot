package com.teto;

import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

public interface IPort extends ITarget{
    default Long getPort(Context ctx, TargetNode node, String serviceName) {
        for(Target target : node.getScannedTargets().getTargets()) {
            if(TargetType.Service.equals(getTargetType(ctx, target))) {
                if(serviceName.equalsIgnoreCase(target.getName())) {
                    return target.getPortNumber();
                }
            }
        }
        return null;
    }
}
