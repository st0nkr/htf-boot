package com.teto.command.searchsploit;

import com.teto.*;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.exec.RunCommand;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.exploit.Exploit;
import com.teto.domain.parser.searchsploit.searchsploit.SearchSploitParser;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import us.springett.parsers.cpe.Cpe;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class SearchSploitOperatingSystem extends AbstractCommand<Collection<Exploit>> 
        implements IScripts, ICPE, IDuration, IVersion {
    private final ScannedTargets st;
    private Target parent;

    public SearchSploitOperatingSystem(Target parent, ScannedTargets st) {
        this.parent = parent;
        this.st = st;
    }

    @Override
    public Optional<Collection<Exploit>> apply(Context ctx) {
        List<Target> osses = st.getOperatingSystems();
        final Collection<Exploit> exploits = new TreeSet<>();
        if(!osses.isEmpty()) {
            for (Target os : osses) {
                if (!os.getCpe().isEmpty()) {
                    Cpe cpe = parseCPE(ctx, os.getCpe());
                    StringBuilder sb = new StringBuilder();
                    sb.append(cpe.getVendor()).append(" ").append(toVersionNumber(cpe.getVersion()));
                    Optional<Script> scp = getScript(ctx, Provenance.SearchSploitKeyTerms);
                    Script script = scp.get();
                    String cmd = script.getCommandLine();
                    cmd = cmd.replace("$keyTerms", sb.toString());
                    info(this, "Executing " + cmd);

                    Optional<RunCommandResponse> rsp = ctx.apply(new RunCommand(cmd, 0, minutes(1)));

                    if (rsp.isPresent()) {
                        script.setLastCommand(cmd);
                        SearchSploitParser parser = new SearchSploitParser();
                        Collection<Exploit> exps = parser.parse(rsp.get().getOutput()).getExploits();
                        exps.forEach(exp -> {
                            exp.setProvenance(Provenance.SearchSploitKeyTerms.name());
                            exp.setTargetId(parent.getId());
                        });
                        exploits.addAll(parser.parse(rsp.get().getOutput()).getExploits());
                    }
                }
            }
        }
        return optional(exploits);
    }

}
