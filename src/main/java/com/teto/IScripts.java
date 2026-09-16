package com.teto;

import com.teto.command.Context;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.script.ScriptManager;

import java.util.Optional;

public interface IScripts extends IOptional{
    default ScriptManager getScriptManager(Context ctx) {
        ScriptManager sm = ctx.fetch(ScriptManager.class);
        if(sm == null) {
            sm = new ScriptManager();
            ctx.stash(ScriptManager.class, sm);
        }
        return sm;
    }

    default Optional<Script> getScript(Context ctx, Provenance prov) {
        ScriptManager sm = getScriptManager(ctx);
        if(sm != null) {
            return optional(sm.getScript(ctx, prov));
        }
        return empty();
    }
}
