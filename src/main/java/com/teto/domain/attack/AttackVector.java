package com.teto.domain.attack;

import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AttackVector {
    private final Target target;
    private final ScannedTargets scannedTargets;
    public AttackVector(Target target, ScannedTargets scannedTargets) {
        this.target = target;
        this.scannedTargets = scannedTargets;
    }
}
