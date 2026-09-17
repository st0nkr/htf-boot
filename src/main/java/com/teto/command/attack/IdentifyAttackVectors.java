package com.teto.command.attack;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.attack.AttackVector;

import java.util.Optional;

public class IdentifyAttackVectors extends AbstractCommand<AttackVector> {
    private final AttackVector root;

    public IdentifyAttackVectors(AttackVector root) {
        this.root = root;
    }

    @Override
    public Optional<AttackVector> apply(Context ctx) {
        String tcpPorts = root.getTarget().getTcpPorts();
        if(tcpPorts != null) {

        }
        String udpPorts = root.getTarget().getUdpPorts();

        return Optional.empty();
    }
}
