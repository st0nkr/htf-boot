package com.teto.domain.attack;

import com.teto.domain.local.TargetNode;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AttackVector {
    private final TargetNode node;

    public AttackVector(TargetNode node) {
        this.node = node;
    }
}
