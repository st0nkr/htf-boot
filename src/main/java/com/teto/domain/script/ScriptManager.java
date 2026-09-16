package com.teto.domain.script;

import com.teto.INMap;
import com.teto.command.Context;
import com.teto.domain.provenance.Provenance;

public class ScriptManager implements INMap {

    public Script getScript(Context ctx, Provenance prov) {
        switch(prov) {
            case DetectUDPServices: return detectUdpServices(ctx);
            case DetectTCPServices: return detectsTCPServices(ctx);
        }
        return null;
    }
}
