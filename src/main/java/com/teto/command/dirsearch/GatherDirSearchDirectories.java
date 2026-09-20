package com.teto.command.dirsearch;

import com.teto.IAttackVector;
import com.teto.IMerge;
import com.teto.IScripts;
import com.teto.ITargetNode;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.local.TargetNode;
import com.teto.domain.parser.dirsearch.DirSearchParser;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GatherDirSearchDirectories extends AbstractCommand<Void> implements ITargetNode, IScripts, IMerge, IAttackVector {
    private final TargetNode node;
    private final Target target;
    public GatherDirSearchDirectories(TargetNode node, Target target) {
        this.node = node;
        this.target = target;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        List<Target> targets = Arrays.asList(target);
        Target http = targets.get(0);
        Optional<Script> script = getScript(ctx, Provenance.DirSearch);
        String fileName = createFileName(ctx, http, script.get());;
        if(isPresent(script)) {
            Optional<RunCommandResponse> rsp = runScript(ctx, http, script.get());
            if(isPresent(rsp)) {
                fileName = rsp.get().getOutputFileName();
            }
        }
        info(this, "Node contains web server..."+http);
        if(fileName != null) {
            DirSearchParser parser = new DirSearchParser();
            ScannedTargets st = parser.parse(ctx, http, fileName);
            node.getScannedTargets().add(st);
        }
        return Optional.empty();
    }
}
