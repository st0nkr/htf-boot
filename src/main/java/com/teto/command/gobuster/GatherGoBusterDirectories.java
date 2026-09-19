package com.teto.command.gobuster;

import com.teto.IAttackVector;
import com.teto.IMerge;
import com.teto.IScripts;
import com.teto.ITargetNode;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.local.TargetNode;
import com.teto.domain.parser.gobuster.GoBusterParser;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.List;
import java.util.Optional;

public class GatherGoBusterDirectories extends AbstractCommand<Void> implements ITargetNode, IScripts, IMerge, IAttackVector {
    private final TargetNode node;

    public GatherGoBusterDirectories(TargetNode node) {
        this.node = node;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        List<Target> targets = getTargets(ctx, node, TargetType.Service, 80l);
        Target http = targets.get(0);
        Optional<Script> script = getScript(ctx, Provenance.GoBusterDir);
        String fileName = createFileName(ctx, http, script.get());;
        if(isPresent(script)) {
            Optional<RunCommandResponse> rsp = runScript(ctx, http, script.get());
            if(isPresent(rsp)) {
                fileName = rsp.get().getOutputFileName();
            }
        }
        info(this, "Node contains web server..."+http);
        if(fileName != null) {
            GoBusterParser parser = new GoBusterParser();
            ScannedTargets st = parser.parse(ctx, http, fileName);
            node.getScannedTargets().add(st);
        }
        return Optional.empty();
    }
}
