package com.teto;

import com.teto.command.Context;
import com.teto.domain.cli.CliArgs;

public interface ICli {
    default CliArgs getCliArgs(Context ctx) {
        return ctx.fetch(CliArgs.class);
    }
}
