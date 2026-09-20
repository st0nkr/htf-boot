package com.teto.command.reconnaissance;

import com.teto.*;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.dirsearch.GatherDirSearchDirectories;
import com.teto.command.gobuster.GatherGoBusterDirectories;
import com.teto.command.merge.MergeScannedTargets;
import com.teto.command.nikto.GatherNiktoData;
import com.teto.command.wordpress.EnumerateWordPressUsers;
import com.teto.domain.local.TargetNode;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.target.Target;
import com.teto.domain.url.Url;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public class Reconnaissance extends AbstractCommand<Void> implements ITargetNode, ISet, IScripts, IUrl, IMerge, IAttackVector {
    private final TargetNode node;

    public Reconnaissance(TargetNode node) {
        this.node = node;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        for(Provenance prov : Provenance.privilegeEscalation()) {
            Optional<Script> scp = getScript(ctx, prov);
            if(isPresent(scp)) {
                String fileName = createFileName(ctx, node.getTarget(), scp.get());
                node.getPrivilegeEscalationScripts().put(prov, fileName);
            }
        }

        info(this, "Reconnoitering node " + node.getTarget().getName());
        ctx.apply(new MergeScannedTargets(node));
        final List<Target> httpTargets = getHTTPTargets(ctx, node, node.getTarget().getIpAddress());
        if (httpTargets != null) {
            info(this, "Detected "+httpTargets.size()+" HTTP Targets");
            for(Target httpTarget : httpTargets) {
                ctx.apply(new GatherDirSearchDirectories(node, httpTarget));
                ctx.apply(new GatherGoBusterDirectories(node, httpTarget));
                ctx.apply(new GatherNiktoData(node, httpTarget));
                Optional<Collection<Url>> wpus = getMatchingUrls(ctx, node, toSet(Provenance.Nikto, Provenance.GoBusterDir, Provenance.DirSearch), "wordpress", "wordpress/");
                if(isPresent(wpus)) {
                    Collection<Url> urls = wpus.get();
                    if(urls != null && !urls.isEmpty()) {
                        ctx.apply(new EnumerateWordPressUsers(node, urls));
                    }
                }

            }
        }

        final List<Target> httpsTargets = getHTTPSTargets(ctx, node, node.getTarget().getIpAddress());
        if (httpsTargets != null) {
            info(this, "Detected "+httpsTargets.size()+" HTTPS Targets");
            for(Target httpsTarget : httpsTargets) {
                ctx.apply(new GatherDirSearchDirectories(node, httpsTarget));
                ctx.apply(new GatherGoBusterDirectories(node, httpsTarget));
                ctx.apply(new GatherNiktoData(node, httpsTarget));
                Optional<Collection<Url>> wpus = getMatchingUrls(ctx, node, toSet(Provenance.Nikto, Provenance.GoBusterDir, Provenance.DirSearch),"wordpress", "wordpress/");
                if(isPresent(wpus)) {
                    Collection<Url> urls = wpus.get();
                    if(urls != null && !urls.isEmpty()) {
                        ctx.apply(new EnumerateWordPressUsers(node, urls));
                    }
                }
            }
        }
        return Optional.empty();
    }
}


