package com.teto.domain.parser.nmap;

import com.teto.IIPAddresses;
import com.teto.IOptional;
import com.teto.IVersionNumber;
import com.teto.command.Context;
import com.teto.command.nmap.CreateNMapScanFromFile;
import com.teto.command.nmap.GuessOSName;
import com.teto.domain.cve.CVE;
import com.teto.domain.difficulty.Difficulty;
import com.teto.domain.nmap.*;
import com.teto.domain.parser.ParserRequest;
import com.teto.domain.port.ServicePort;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.service.KnownService;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.*;

import static com.teto.domain.difficulty.Difficulty.GoodLuck;

public class NMapParser implements IVersionNumber,INMapUtils,IOptional, IIPAddresses {

    public ScannedTargets parse(Context ctx, ParserRequest req) {
        ScannedTargets ret = new ScannedTargets();
        final Provenance provenance = req.getProvenance();
        final Optional<NmapRun> scan = ctx.apply(new CreateNMapScanFromFile(req.getOutputFileName(), req.getProvenance().name()));

        if (isPresent(scan)) {
            List<Host> hosts = scan.get().getHosts();
            for (Host host : hosts) {
                Target parent = createTarget(req.getParent(), host);
                List<Port> openPorts = getOpenPorts(host);
                if (openPorts != null && !openPorts.isEmpty()) {
                    addPorts(parent, openPorts);
                }
                Integer mtu = scan.get().getMTU();
                if (mtu != null) {
                    parent.setMtu(mtu);
                }
                Address addr = mergeAddresses(host.getAddresses());
                parent.setMacAddress(addr.getMacAddr());
                parent.setIpAddress(addr.getAddr());
                parent.setTargetType(addr.getAddrType());
                parent.setVendor(addr.getVendor());
                parent.setName(addr.getVendor());
                ret.getTargets().add(parent);

                final List<String> hops = scan.get().getHops(host);
                if (hops != null && !hops.isEmpty()) {
                    for (String hop : hops) {
                        if (!hop.equals(req.getParent().getIpAddress())) {
                            Target t = new Target();
                            t.setProvenance(provenance.name());
                            t.setParentType(parent.getTargetType());
                            t.setLevel(parent.getLevel() + 1);
                            t.setIpAddress(hop);
                            if (isValidIPV4(hop)) {
                                t.setTargetType(TargetType.Ipv4.name());
                            } else {
                                if (isValidIPV6(hop)) {
                                    t.setTargetType(TargetType.Ipv6.name());
                                }
                            }
                            ret.getTargets().add(t);
                        }
                    }
                }
                List<ServicePort> servicePorts = createServicePorts(ctx, parent, provenance, getAllPorts(host));
                ret.getServicePorts().addAll(servicePorts);
                parent.setSuitableForIdleScan(isSuitableForIdleScan(ctx, host));
                parent.setSuitableForZombieScan(isSuitableForZombieScan(ctx, host));
                parent.setDifficulty(getScanDifficulty(ctx, host));
                Target osTarget = getOperatingSystem(ctx, host);
                if (osTarget == null) {
                    osTarget = req.getParent();
                } else {
                    osTarget.setLevel(req.getParent().getLevel() + 1);
                    osTarget.setUri(req.getParent().getUri());
                    osTarget.setParentId(req.getParent().getId());
                    osTarget.setParentType(req.getParent().getTargetType());
                    osTarget.setTargetType(TargetType.OperatingSystem.name());
                    osTarget.setUri("?");
                    osTarget.setName("?");

                    Optional<String> osName = ctx.apply(new GuessOSName(host));
                    if (isPresent(osName)) {
                        osTarget.setUnderlyingSystem(osName.get());
                        osTarget.setName(osName.get());
                        osTarget.setUri(osName.get());
                    }
                    if (isEmptyString(osTarget.getIpAddress())) {
                        String ip = getIpAddress(osTarget.getUnderlyingCpe());
                        osTarget.setIpAddress(ip);
                    }
                }
                ret.getTargets().add(osTarget);
                final Collection<Target> cpes = createCPEs(ctx, host);
                if (cpes != null) {
                    ret.getTargets().addAll(cpes);
                }

                final List<Target> techs = getProductTechs(ctx, host);
                if (techs != null && !techs.isEmpty()) {

                    techs.forEach(tech -> {
                        tech.setIpAddress(req.getParent().getIpAddress());
                        String version = getVersionNumber(ctx, tech.getName());
                        if (version != null) {
                            tech.setVersion(version);
                        }
                        if (req.getParent().getUnderlyingSystem() == null) {
                            if (tech.getUnderlyingSystem() != null) {
                                req.getParent().setUnderlyingSystem(tech.getUnderlyingSystem());
                                req.getParent().setOsFamily(tech.getOsFamily());
                            }
                        }
                        tech.setParentId(req.getParent().getId());
                        tech.setParentType(req.getParent().getTargetType());
                        tech.setUri(req.getParent().getUri());
                        tech.setProvenance(provenance.name());
                        tech.setLevel(req.getParent().getLevel() + 1);
                    });
                    ret.getTargets().addAll(techs);
                }

                final List<CVE> cves = getCVEs(ctx, host);
                if (cves != null && !cves.isEmpty()) {
                    final Target finalOsTarget1 = osTarget;
                    cves.forEach(cve -> {
                        cve.setSource(finalOsTarget1.getUri());
                        cve.setParentId(req.getParent().getId());
                        cve.setProvenance(provenance);
                        cve.setUnderlyingCpe(finalOsTarget1.getCpe());
                        cve.setParentType(req.getParent().getTargetType());
                        cve.setUnderLyingSystem(finalOsTarget1.getUnderlyingSystem());
                        cve.setParentId(req.getParent().getId());
                        cve.setLevel(req.getParent().getLevel() + 1);
                    });
                    ret.getCves().addAll(cves);
                }
                final List<Target> services = createServiceTargets(ctx, host, osTarget, ret.getServicePorts());

            if (services != null && !services.isEmpty()) {
                final Target finalOsTarget = osTarget;
                services.forEach(s -> {
                    s.setIpAddress(req.getParent().getIpAddress());
                    s.setParentUri(finalOsTarget.getUri());
                    s.setParentId(req.getParent().getId());
                    s.setParentType(req.getParent().getTargetType());
                });

                List<NSEScript> scripts = createScripts(ctx, host);
                if (scripts != null && !scripts.isEmpty()) {
                    for (NSEScript script : scripts) {
                        if (script.getServicePorts() != null) {
                            for (ServicePort sp : script.getServicePorts()) {
                                sp.setProvenance(provenance.name());
                                sp.setParentId(req.getParent().getId());
                                sp.setIpAddress(req.getParent().getIpAddress());
                            }
                            ret.getServicePorts().addAll(script.getServicePorts());
                        }
                        if (script.getInfo() != null) {
                            NSEInfo info = script.getInfo();
                            if (info.getTargets() != null) {
                                for (TargetType tt : info.getTargets().keySet()) {
                                    Collection<String> targets = info.getTargets().get(tt);
                                    for (String target : targets) {
                                        Target ua = new Target();
                                        ua.setTargetType(tt.name());
                                        ua.setParentId(req.getParent().getId());
                                        ua.setParentType(req.getParent().getTargetType());
                                        ua.setUri(target);
                                        ua.setName(tt.name());
                                        ua.setLevel(req.getParent().getLevel() + 1);
                                        ua.setVerified(Boolean.TRUE);
                                        ua.setPortNumber(script.getPortId());
                                        ua.setProvenance(provenance.name());
                                        ret.getTargets().add(ua);
                                    }
                                }
                            }
                        }
                        if (script.getWafs() != null) {
                            script.getWafs().forEach(waf -> {
                                waf.setLevel(req.getParent().getLevel() + 1);
                                waf.setProvenance(provenance.name());
                                waf.setParentType(req.getParent().getTargetType());
                                waf.setParentId(req.getParent().getId());
                            });
                            ret.getWafs().addAll(script.getWafs());
                        }
                        if (script.getTables() != null && !script.getTables().isEmpty()) {
                            for (Table table : script.getTables()) {
                                String key = table.getKey();
                                if ("Allowed User Agents".equals(key)) {
                                    if (table.getElems() != null && !table.getElems().isEmpty()) {

                                        for (Elem agent : table.getElems()) {
                                            Target ua = new Target();
                                            ua.setTargetType(TargetType.UserAgent.name());
                                            ua.setParentId(req.getParent().getId());
                                            ua.setParentType(req.getParent().getTargetType());
                                            ua.setUri(agent.getValue());
                                            ua.setName(agent.getValue());
                                            ua.setLevel(req.getParent().getLevel() + 1);
                                            ua.setVerified(Boolean.TRUE);
                                            ua.setPortNumber(script.getPortId());
                                            ua.setProvenance(provenance.name());
                                            ret.getTargets().add(ua);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                for (var service : services) {
                    service.setParentId(req.getParent().getId());
                    service.setParentType(req.getParent().getTargetType());
                    service.setLevel(req.getParent().getLevel() + 1);
                    service.setVerified(Boolean.TRUE);
                    //service.setIpAddress(osTarget.getIpAddress());
                    service.setUnderlyingSystem(osTarget.getTargetType());
                    service.setProduct(osTarget.getProduct());
                    service.setCpe(osTarget.getCpe());
                    service.setUnderlyingSystem(osTarget.getUnderlyingSystem());
                    service.setUnderlyingCpe(osTarget.getCpe());
                    service.setProduct(osTarget.getProduct());

                    service.setProvenance(provenance.name());
                    service.setTargetMainType(TargetType.Service.name());
                    service.setTargetType(TargetType.Service.name());
                    if (isLinux(osTarget) && isNFSOrIIS(service)) {
                        service.setTargetType(TargetType.NFS.name());
                    } else {
                        if (service.getName().equalsIgnoreCase("fwl-or-bgmp")) {
                            service.setTargetType(TargetType.Firewall.name());
                        }
                    }
                }
                ret.getTargets().addAll(services);
            }
        }
    }
        for(Target target : ret.getTargets()) {
            //target.setIpAddress(req.getParent().getIpAddress());
            target.setProvenance(provenance.name());
        }
        ret.getCves().forEach(cve -> {
            cve.setProvenance(provenance);
        });
        return ret;
    }

    private Target createTarget(Target parent, Host host) {
        Target t = new Target();
        t.setParentType(parent.getTargetType());
        t.setLevel(parent.getLevel()+1);
        t.setParentId(parent.getId());

        return t;
    }

    private List<ServicePort> createServicePorts(Context ctx, Target parent, Provenance prov, List<Port> ports) {
        final List<ServicePort> sports = new ArrayList<>();
        for(Port port : ports) {
            ServicePort sp = createServicePort(ctx,port);
            if(sp != null) {
                sp.setProvenance(prov.name());
                sp.setParentId(parent.getId());
                sp.setParentType(parent.getTargetType());
                sp.setLevel(parent.getLevel() + 1);
                sp.setIpAddress(parent.getIpAddress());
                sports.add(sp);
            }
        }
        return sports;
    }

    private ServicePort createServicePort(Context ctx, Port port) {
        Service service = port.getService();
        if(service == null) {
            return null;
        }
        ServicePort sp = new ServicePort();
        sp.setProtocol(port.getProtocol());
        sp.setPortState(port.getState().getState());
        sp.setPortNumber(port.getPortId());

        sp.setProduct(service.getProduct());
        if ("tcpwrapped".equals(service.getName()) || isEmptyString(service.getName())) {
            List<KnownService> knowns = getKnownServices(ctx, port.getPortId(), port.getProtocol());
            if(knowns != null && !knowns.isEmpty()) {
                sp.setName(knowns.get(0).getName());
            } else {
                String serviceName = service.getName();
                if(service.getTunnel() != null && "ssl".equals(service.getTunnel())) {
                    if("http".equals(serviceName)) {
                        serviceName = TargetType.Https.name();
                    }
                    if("ftps".equals(serviceName)) {
                        serviceName = TargetType.Ftps.name();
                    }
                    if("ftp".equals(serviceName)) {
                        serviceName = TargetType.Ftp.name();
                    }
                }
                sp.setName(serviceName);
            }
        } else {
            String serviceName = service.getName();
            if(service.getTunnel() != null && "ssl".equals(service.getTunnel())) {
                if("http".equals(serviceName)) {
                    serviceName = TargetType.Https.name();
                }
                if("ftps".equals(serviceName)) {
                    serviceName = TargetType.Ftps.name();
                }
                if("ftp".equals(serviceName)) {
                    serviceName = TargetType.Ftp.name();
                }
            } else {
                switch(serviceName) {
                    case "rpcbind" : {
                        List<KnownService> knowns = getKnownServices(ctx, sp.getPortNumber(), sp.getProtocol());
                        if(knowns != null && knowns.size() == 1) {
                            KnownService ks = knowns.get(0);
                            serviceName = ks.getComment();
                            sp.setAccuracy(50);
                        }
                    } break;
                    case "status": {
                        if(service.getExtraInfo() != null && service.getExtraInfo().startsWith("RPC")) {
                            serviceName = "Remote Procedure Call";
                        }
                    } break;
                    case "https":
                    case "http":
                    case "ftp":
                    case "ftps":
                    case "ldap":
                    case "domain":
                    case "http-proxy":
                    case "java-rmi":
                    case "ssh": break;
                    default:
                        System.out.println("ServiceName unknown ===>"+serviceName);
                }
            }
            sp.setName(serviceName);
        }
        return sp;
    }

    private void addPorts(Target parent, List<Port> ports) {

        Set<String> tcps = new HashSet<>();
        for(String tcp : Arrays.asList(parent.getTcpPorts().split(","))) {
            if(!tcp.isEmpty()) {
                tcps.add(tcp);
            }
        }
        Set<String> udps = new HashSet<>();
        for(String udp : Arrays.asList(parent.getUdpPorts().split(","))) {
            if(!udp.isEmpty()) {
                udps.add(udp);
            }
        }
        for(Port port : ports) {
            if("tcp".equals(port.getProtocol())) {
                tcps.add(port.getPortId()+"");
            } else {
                if("udp".equals(port.getProtocol())) {
                    udps.add(port.getPortId()+"");
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for(String tcp : tcps) {
            sb.append(tcp).append(",");
        }
        parent.setTcpPorts(removeLast(sb.toString()));
        sb = new StringBuilder();
        for(String udp : udps) {
            sb.append(udp).append(",");
        }
        parent.setUdpPorts(removeLast(sb.toString()));

    }

    private boolean isPortOpen(Port p) {
        if(p != null) {
            State state = p.getState();
            if(state != null) {
                if ("open".equals(state.getState())) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Port> getAllPorts(Host host) {
        final List<Port> ports = new ArrayList<>();
        if(host.getPorts() != null && host.getPorts().getPorts() != null) {
            ports.addAll(host.getPorts().getPorts());
        }
        return ports;
    }
    private List<Port> getOpenPorts(Host host) {
        final List<Port> ports = new ArrayList<>();
        if (host.getPorts() != null && host.getPorts().getPorts() != null) {
            host.getPorts().getPorts().forEach(port -> {
                if (isPortOpen(port)) {
                    ports.add(port);
                }
            });
        }
        return ports;
    }

    private String getScanDifficulty(Context ctx, Host host) {
        TcpSequence seq = getTcpSSequence(ctx, host);
        if(seq == null) {
            return GoodLuck.name();
        }
        return Difficulty.fromIndex(seq.getIndex()).name();
    }

    private Boolean isSuitableForZombieScan(Context ctx, Host host) {
        IpIdSequence seq = getIpIdSequence(ctx, host);
        if(seq == null) {
            return false;
        }
        String clazz = seq.getClazz().toLowerCase();
        if(clazz.contains("incremental")) {
            return true;
        }
        return false;
    }

    private boolean isSuitableForIdleScan(Context ctx, Host host) {
        IpIdSequence seq = getIpIdSequence(ctx, host);
        if(seq == null) {
            return false;
        }
        String clazz = seq.getClazz().toLowerCase();
        if(clazz.contains("constant") || clazz.contains("random")) {
            return false;
        }
        if(clazz.contains("unknown class")) {
            return false;
        }
        return true;
    }

    private TcpSequence getTcpSSequence(Context ctx, Host host) {
        return host.getTcpSequence();
    }
    private IpIdSequence getIpIdSequence(Context ctx, Host host) {
        return host.getIpIdSequence();
    }

    private boolean isNFSOrIIS(Target t) {
        if(t.getName().equalsIgnoreCase("NFS-or-IIS")) {
            return true;
        }
        return false;
    }

    private boolean isLinux(Target t) {
        String uos = t.getUnderlyingSystem();
        if("linux".equalsIgnoreCase(uos)) {
            return true;
        }
        return false;
    }

}