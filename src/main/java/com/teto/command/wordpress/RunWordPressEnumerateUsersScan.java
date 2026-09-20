package com.teto.command.wordpress;

import com.teto.IScriptArgProvide;
import com.teto.IScripts;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.local.TargetNode;
import com.teto.domain.parser.wordpress.WordPressParser;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.url.Url;

import java.util.Collection;
import java.util.Optional;

public class RunWordPressEnumerateUsersScan extends AbstractCommand<ScannedTargets> implements IScripts, IScriptArgProvide {
    private final TargetNode node;
    private final Collection<Url> wordPressUrls;

    public RunWordPressEnumerateUsersScan(TargetNode node, Collection<Url> wordPressUrls) {
        this.node = node;
        this.wordPressUrls = wordPressUrls;
    }

    @Override
    public Optional<ScannedTargets> apply(Context ctx) {
        ScannedTargets st = new ScannedTargets();
        final Target target = node.getTarget();
        Optional<Script> scp = getScript(ctx, Provenance.WordPressEnumerateUsers);
        String fileName = null;
        if(isPresent(scp)) {
            final Script script = scp.get();
            fileName = createFileName(ctx, node.getTarget(), script);
            String url = wordPressUrls.iterator().next().getUrl();
            Optional<RunCommandResponse> rsp = runScript(ctx, node.getTarget(), script, sap(ctx,target,script,url));
            if(isPresent(rsp)) {
                fileName = rsp.get().getOutputFileName();
            }
        }
        if(fileName != null) {
            WordPressParser parser = new WordPressParser();
            ScannedTargets stargs = parser.parse(ctx, node.getTarget(), fileName);

            if(!node.getWordpressUsers().isEmpty()) {
                stargs.getUsers().addAll(node.getWordpressUsers());
            }
            node.getScannedTargets().add(stargs);
        }
        return optional(st);
    }
}
