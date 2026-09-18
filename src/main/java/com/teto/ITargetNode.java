package com.teto;

import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.target.TargetType;

public interface ITargetNode extends ITarget{
    default boolean isA(Context ctx, TargetType tt, TargetNode node) {
        TargetType tt2 = getTargetType(ctx, node.getTarget());
        return tt.equals(tt2);
    }

    default boolean isAVirtualBox(Context ctx, TargetNode node) {
        return isA(ctx, TargetType.VirtualBox, node);
    }
}
