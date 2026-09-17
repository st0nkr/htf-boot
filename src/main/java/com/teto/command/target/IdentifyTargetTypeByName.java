package com.teto.command.target;

import com.teto.ICSV;
import com.teto.ITarget;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.Optional;

public class IdentifyTargetTypeByName extends AbstractCommand<Void> implements ICSV, ITarget {
    private final Target target;

    public IdentifyTargetTypeByName(Target target) {
        this.target = target;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        String name = target.getName();
        if(name != null) {
            TargetType tt = getTargetType(ctx, name);
            if (tt != null) {
                target.setTargetType(tt.name());
            }
        }
        return Optional.empty();
    }
}
