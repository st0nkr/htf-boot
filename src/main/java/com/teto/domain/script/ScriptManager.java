package com.teto.domain.script;

import com.teto.*;
import com.teto.command.Context;
import com.teto.domain.provenance.Provenance;

public class ScriptManager implements INMap, IWebTech, IWhatWeb, IWappy,ISearchSploit, IKatana,IDirBuster,IArp, INikto,ILinPeas,IWPScan, ILinEnum, IDirSearch, IHydra, IGoBuster {

    public Script getScript(Context ctx, Provenance prov) {
        switch(prov) {
            case WebTech: return webTech(ctx);
            case WhatWeb: return whatWeb(ctx);
            case Wappy: return wappy(ctx);
            case Katana : return katana(ctx);
            case DirBuster: return dirBuster(ctx);
            case Nikto: return nikto(ctx);
            case DirSearch: return dirSearch(ctx);
            case LinEnum: return linEnum(ctx);
            case LinPeas: return linPeas(ctx);
            case HydraSSH: return hydraSSH(ctx);
            case WordPressEnumerateUsers: return wordPressEnumerateUsers(ctx);
            case GoBusterDir: return goBusterDir(ctx);
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
