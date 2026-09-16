package com.teto.command.script;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.target.ScannedTargets;

import java.util.Optional;

public class RunScript extends AbstractCommand<ScannedTargets> {
    @Override
    public Optional<ScannedTargets> apply(Context ctx) {
        // Create CliMapper
        // Create CommandLine
        // Run script
        // Parse output
        return Optional.empty();
    }
}
