package com.teto.command.katana;

import com.teto.IAttackVector;
import com.teto.IMerge;
import com.teto.IScripts;
import com.teto.ITargetNode;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.local.TargetNode;
import com.teto.domain.parser.gobuster.GoBusterParser;
import com.teto.domain.parser.katana.KatanaParser;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GatherKatanaDirectories extends AbstractCommand<Void> implements ITargetNode, IScripts, IMerge, IAttackVector {
    private final TargetNode node;
    private final Target target;

    public GatherKatanaDirectories(TargetNode node, Target target) {
        this.node = node;
        this.target = target;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        List<Target> targets = Arrays.asList(target);
        Target http = targets.get(0);
        Optional<Script> script = getScript(ctx, Provenance.Katana);
        String fileName = createFileName(ctx, http, script.get());;
        if(isPresent(script)) {
            Optional<RunCommandResponse> rsp = runScript(ctx, http, script.get());
        }
        info(this, "Node contains web server..."+http);
        if(fileName != null) {
            KatanaParser parser = new KatanaParser();
            ScannedTargets st = parser.parse(ctx, http, fileName);
            node.getScannedTargets().add(st);
        }
        return Optional.empty();
    }
}
