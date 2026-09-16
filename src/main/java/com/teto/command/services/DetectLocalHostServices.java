package com.teto.command.services;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.local.LocalTarget;
import com.teto.domain.target.Target;

import java.util.List;
import java.util.Optional;

public class DetectLocalHostServices extends AbstractCommand<List<Target>> {
    private final LocalTarget localTarget;

    public DetectLocalHostServices(LocalTarget localTarget) {
        this.localTarget = localTarget;
    }

    @Override
    public Optional<List<Target>> apply(Context ctx) {
        return Optional.empty();
    }
}
