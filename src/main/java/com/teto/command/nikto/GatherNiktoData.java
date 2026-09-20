package com.teto.command.nikto;

import com.teto.IAttackVector;
import com.teto.IMerge;
import com.teto.IScripts;
import com.teto.ITargetNode;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.local.TargetNode;
import com.teto.domain.parser.gobuster.GoBusterParser;
import com.teto.domain.parser.nikto.NiktoParser;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GatherNiktoData extends AbstractCommand<Void> implements ITargetNode, IScripts, IMerge, IAttackVector {
    private final TargetNode node;
    private final Target target;

    public GatherNiktoData(TargetNode node, Target target) {
        this.node = node;
        this.target = target;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        Optional<Script> script = getScript(ctx, Provenance.Nikto);
        String fileName = createFileName(ctx, target, script.get());;
        if(isPresent(script)) {
            Optional<RunCommandResponse> rsp = runScript(ctx, target, script.get());
        }
        if(fileName != null) {
            NiktoParser parser = new NiktoParser();
            ScannedTargets st = parser.parse(ctx, target, fileName);
            node.getScannedTargets().add(st);
        }
        return Optional.empty();
    }
}
