package com.teto.command.framework;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.database.LoadKnownServices;

import java.util.Optional;

public class InitialiseDatabase extends AbstractCommand<Void> {
    @Override
    public Optional<Void> apply(Context ctx) {
        ctx.apply(new LoadKnownServices());
        return Optional.empty();
    }
}
