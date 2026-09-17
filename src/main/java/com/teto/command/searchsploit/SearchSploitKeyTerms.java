package com.teto.command.searchsploit;

import com.teto.IDuration;
import com.teto.IScripts;
import com.teto.ISearchSploit;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommand;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.exploit.Exploit;
import com.teto.domain.parser.searchsploit.searchsploit.SearchSploitParser;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.searchsploit.SearchSploitRequest;
import com.teto.domain.target.ScannedTargets;

import java.util.Collection;
import java.util.Optional;
import java.util.TreeSet;

public class SearchSploitKeyTerms extends AbstractCommand<Collection<Exploit>>
        implements ISearchSploit,IScripts, IDuration {
    private final ScannedTargets st;
    private final SearchSploitRequest req;

    public SearchSploitKeyTerms(ScannedTargets st, SearchSploitRequest req) {
        this.st = st;
        this.req = req;
    }

    @Override
    public Optional<Collection<Exploit>> apply(Context ctx) {
        Optional<Script> scp = getScript(ctx, Provenance.SearchSploitKeyTerms);
        if(isEmpty(scp)) {
            return empty();
        }
        Script script = scp.get();
        String cmd = script.getCommandLine();
        if(req.isCaseSensitive()) {
            cmd = addCaseSensitive(ctx, cmd);
        }
        if(req.isExactMatch()) {
            cmd = addExactMatch(ctx, cmd);
        }
        if(req.isRequireAllItemsMatch()) {
            cmd = addStrictMatch(ctx, cmd);
        }
        if(!req.getExcludes().isEmpty()) {
            cmd = addExcludes(ctx, cmd, req.getExcludes().toArray(new String[0]));
        }
        cmd = cmd.replace("$keyTerms",getKeySearchTerms(req));

        info(this, "Executing " + cmd);

        Optional<RunCommandResponse> rsp = ctx.apply(new RunCommand(cmd, 0, minutes(1)));
        Collection<Exploit> exploits = new TreeSet<>();
        if(rsp.isPresent()) {
            script.setLastCommand(cmd);
            SearchSploitParser parser = new SearchSploitParser();
            exploits.addAll(parser.parse(rsp.get().getOutput()).getExploits());
        }
        return optional(exploits);
    }

    private String getKeySearchTerms(SearchSploitRequest req) {
        return concat(req.getKeyTerms()," ");
    }
}
