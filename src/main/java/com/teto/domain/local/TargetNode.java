package com.teto.domain.local;

import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
public class TargetNode {
    private final Target target;
    private TargetType targetType;
    private Collection<Target> services = new ArrayList<>();
    private ScannedTargets scannedTargets;
    private Map<Provenance, String> privilegeEscalationScripts = new HashMap<>();
    private List<String> directoriesCreated = new ArrayList<>();
    public TargetNode(Target target) {
        this.target = target;
    }
}
