package com.teto;

import com.teto.command.Context;
import com.teto.domain.file.FileExtension;
import com.teto.domain.meta.Tag;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.script.ScriptCategory;

public interface INMap extends IProperties{

    default boolean isNMap(Context ctx, Script script) {
        Provenance prov = Provenance.fromString(script.getName());
        if(prov == null) {
            return false;
        }
        if(script.getExecutable().contains("nmap")) {
            return true;
        }
        return false;
    }
    default Script ftpSyst(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.FtpBrute.name());
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "-sX --spoof-mac $spoofMac -D $decoyIps(5) --script ftp-syst --mtu $mtu(100:600) -p $port(ftp) $ip -Pn -b -v -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        return s;
    }
    default Script ftpBounce(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.FtpBrute.name());
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "-sX --spoof-mac $spoofMac -D $decoyIps(5) --mtu $mtu(100:600) -p $scanPorts $ip -Pn -b -v -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        return s;
    }

    default String locateExecutable(Context ctx, String nmap) {
        return nmap;
    }

    default Script xmasScan(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.XMasScan.name());
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "--spoof-mac $spoofMac -D $decoyIps(5) --mtu $mtu(100:600) -p $scanPorts $ip -sX -v -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        return s;
    }

    default Script windowScan(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.WindowScan.name());
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "--spoof-mac $spoofMac -D $decoyIps(5) --mtu $mtu(100:600) -p $scanPorts $ip -sW -v -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        return s;
    }
    default Script maimonScan(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.MaimonScan.name());
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "--spoof-mac $spoofMac -D $decoyIps(5) --mtu $mtu(100:600) $ip -p $scanPorts -sM -v -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        return s;
    }

    default Script zombieScan(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.ZombieScan.name());
        s.setProxyChains(true);
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "--spoof-mac $spoofMac -D $decoyIps(5) --mtu $mtu(100:600) $ip -sI -Pn -p $scanPorts -v -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        return s;
    }

    default Script script(Context ctx, Provenance prov, ScriptCategory cat, String scriptNames) {
        Script s = new Script();
        s.setName(prov.name());
        s.setProxyChains(true);
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "--spoof-mac $spoofMac -D $decoyIps(5) --mtu $mtu(100:600) $ip -sV -sX -v --script \""+scriptNames+"\" -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        s.setSubCategory(cat.name());
        return s;
    }
    default Script nmapFTP(Context ctx) {
        return script(ctx, Provenance.NMapFTP,ScriptCategory.FTP,"*ftp*");
    }

    default Script nmapNFS(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.NMapNFS.name());
        s.setProxyChains(true);
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "--spoof-mac $spoofMac -D $decoyIps(5) --mtu $mtu(100:600) $ip -sV -sX -v --script \"*nfs*\" -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        s.setSubCategory(ScriptCategory.NFS.name());
        return s;
    }



    default Script httpWafDetect(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.HttpWafDetect.name());
        s.setProxyChains(true);
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "--spoof-mac $spoofMac -D $decoyIps(5) --mtu $mtu(100:600) $ip -sV -sX -v --script "+Provenance.HttpWafDetect.getTag()+" -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        s.setSubCategory(ScriptCategory.WAF.name());
        return s;
    }
    default Script wafFingerprint(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.HttpWafFingerprint.name());
        s.setProxyChains(true);
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "-sX --spoof-mac $spoofMac -D $decoyIps(3) --mtu $mtu(100:600) $ip -sV -v --script "+Provenance.HttpWafFingerprint.getTag()+" -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        s.setSubCategory(ScriptCategory.WAF.name());
        return s;
    }
    default Script vulscan(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.Vulscan.name());
        s.setProxyChains(true);
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "-sX --spoof-mac $spoofMac $ip -sV -v -p $scanPorts --script vulscan/vulscan.nse -oX $xml";
        String normal = "$ip -sV -v -p- --script vulscan/vulscan.nse -oX $xml";
        s.setNormalCommandLine(s.getExecutable()+" "+normal);
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        s.setSubCategory(ScriptCategory.VULNERABILITY.name());
        return s;
    }
    default Script httprecon(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.HttpRecon.name());
        s.setProxyChains(true);
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "-sX --spoof-mac $spoofMac $ip -sV -v -p $scanPorts --script httprecon -n -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        String normal = "$ip -sC -sV -p $scanPorts --script httprecon/httprecon.nse -n -oX $xml";
        s.setNormalCommandLine(s.getExecutable()+" "+normal);
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        return s;
    }

    default Script pathMtu(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.PathMtu.name());
        s.setProxyChains(true);
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "-sX --spoof-mac $spoofMac $ip --traceroute --script path-mtu --script-args http.useragent=$userAgent(random) --reason --source-port $sourcePort(53,20,88,67) --mtu $mtu(100:500) --script-trace -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        return s;
    }

    default Script broadCastXdmcp(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.BroadcastXdmcpDiscover.name());
        s.setProxyChains(true);
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "-sX --spoof-mac $spoofMac $ip -p $sourcePorts -v --script broadcast-xdmcp-discover -oX $xml";
        String normal = "-vv --reason -Pn -T4 -sU -p- -A -v -O $ip -oX $xml";
        s.setNormalCommandLine(s.getExecutable()+" "+normal);
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        return s;
    }
    default Script firewalk(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.Firewalk.name());
        s.setProxyChains(true);
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "-sX --spoof-mac $spoofMac $ip -sV -v --script firewalk -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        s.setSubCategory(ScriptCategory.WAF.name());
        return s;
    }

    default Script finScan(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.FINScan.name());
        s.setProxyChains(true);
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "--spoof-mac $spoofMac -D $decoyIps(5) -p $scanPorts --mtu $mtu(100:600) $ip -sF -v -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        return s;
    }

    default Script nullScan(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.NullScan.name());
        s.setProxyChains(true);
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "--spoof-mac $spoofMac -D $decoyIps(5) -p $scanPorts --mtu $mtu(100:600) $ip -sN -v -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());

        return s;
    }


    default Script bannerGrab(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.BannerGrab.name());
        s.setProxyChains(true);
        s.setExecutable("bg");
        String cmd = "";
        s.setOutputFormat(FileExtension.json.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.BannerGrab.name());
        return s;
    }
    default Script httpProxyBrute(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.HttpProxyBrute.name());
        s.setProxyChains(true);
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "-sX --spoof-mac $spoofMac -p $scannedPorts(http-proxy) $ip --script http-proxy-brute -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        s.setSubCategory(ScriptCategory.NamedService.name());
        return s;
    }

    default Script nmapBanner(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.NMapBanner.name());
        s.setProxyChains(true);
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "-sX --spoof-mac $spoofMac -sV $ip --script banner -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        s.setSubCategory(ScriptCategory.NamedService.name());
        return s;
    }
    default Script httpOpenProxy(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.HttpOpenProxy.name());
        s.setProxyChains(true);
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "-sX --spoof-mac $spoofMac -p $scannedPorts(http-proxy) --script http-open-proxy $ip -sV -A -O -T5 -v -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        s.setSubCategory(ScriptCategory.NamedService.name());
        return s;
    }
    default Script aggressiveNMap(Context ctx) {
        Script s = new Script();
        s.setProxyChains(true);
        s.setName(Provenance.AggressiveNMap.name());
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "-sX --spoof-mac $spoofMac -D $decoyIps(5) -p $scannedPorts --mtu $mtu(100:600) $ip -sV -A -O -T5 -v -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());

        return s;
    }

    default Script pingScan(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.PingScan.name());
        s.setSudo(true);
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = "--spoof-mac $spoofMac $ip -PN -T5 -sT -sU -p $scanPorts -A --min-parallelism $minParallelism -oX $xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        return s;
    }

    default Script quickLocalNetworkScan(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.QuickLocalNetworkScan.name());
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = property(ctx, Tag.QuickLocalNetworkScan);
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        return s;
    }
    default Script detectAllServices(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.DetectAllServices.name());
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = property(ctx, Tag.DetectAllServices);
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        return s;
    }
    default Script detectUdpServices(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.DetectUDPServices.name());
        s.setExecutable(locateExecutable(ctx,"nmap"));
        String cmd = property(ctx, Tag.DetectUDPServices);
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        return s;
    }
    default Script detectsTCPServices(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.DetectTCPServices.name());
        s.setExecutable(locateExecutable(ctx,"nmap"));

        String cmd = property(ctx, Tag.DetectTCPServices);
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.NMAP.name());
        return s;
    }
}
