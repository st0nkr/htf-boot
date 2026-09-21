package com.teto.command.dirbuster;

import com.teto.*;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.local.TargetNode;
import com.teto.domain.parser.dirbuster.DirBusterParser;
import com.teto.domain.parser.gobuster.GoBusterParser;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GatherDirBusterDirectories extends AbstractCommand<Void> implements IScriptArgProvide, ITargetNode, IScripts, IMerge, IAttackVector {
    private final TargetNode node;
    private final Target target;

    public GatherDirBusterDirectories(TargetNode node, Target target) {
        this.node = node;
        this.target = target;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        List<Target> targets = Arrays.asList(target);
        Target http = targets.get(0);
        Optional<Script> script = getScript(ctx, Provenance.DirBuster);
        String fileName = createFileName(ctx, http, script.get());;
        if(isPresent(script)) {
            Optional<RunCommandResponse> rsp = runScript(ctx, http, script.get(), sap(ctx,node, http,script.get()));
        }
        info(this, "Node contains web server..."+http);
        if(fileName != null) {
            DirBusterParser parser = new DirBusterParser();
            ScannedTargets st = parser.parse(ctx, http, fileName);
            node.getScannedTargets().add(st);
        }
        return Optional.empty();
    }
}
