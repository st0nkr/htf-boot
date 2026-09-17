package com.teto.domain.script;

import com.teto.IArp;
import com.teto.INMap;
import com.teto.ISearchSploit;
import com.teto.command.Context;
import com.teto.domain.provenance.Provenance;

public class ScriptManager implements INMap, ISearchSploit, IArp {

    public Script getScript(Context ctx, Provenance prov) {
        switch(prov) {
            case ArpNames: return arpNames(ctx);
            case QuickLocalNetworkScan: return quickLocalNetworkScan(ctx);
            case DetectAllServices: return detectAllServices(ctx);
            case DetectUDPServices: return detectUdpServices(ctx);
            case DetectTCPServices: return detectsTCPServices(ctx);
            case SearchSploitNMAP: return searchSploitNMAP(ctx);
            case SearchSploitKeyTerms: return searchSploitKeyTerms(ctx);
        }
        return null;
    }
}
