package com.teto.command.peas;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.local.TargetNode;

import java.util.Optional;

public class DetermineWindowsAttackVectorsFromPeas extends AbstractCommand<Void> {
    private final TargetNode node;

    public DetermineWindowsAttackVectorsFromPeas(TargetNode node) {
        this.node = node;
    }

    @Override
    public Optional<Void> apply(Context ctx) {

        return Optional.empty();
    }
}
