package com.teto.domain.parser.nmap;

import com.teto.IKnownServices;
import com.teto.IString;
import com.teto.command.Context;
import com.teto.domain.cve.CVE;
import com.teto.domain.nmap.*;
import com.teto.domain.port.PortState;
import com.teto.domain.product.Product;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.reason.Reason;
import com.teto.domain.service.KnownService;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;
import us.springett.parsers.cpe.Cpe;

import java.util.*;


public interface INMapUtils extends IString, IKnownServices {

    default List<Port> locatePorts(Context ctx, Host host) {
        if(host.getPorts() != null) {
            return host.getPorts().getPorts();
        }
        return Collections.emptyList();
    }

    default List<Port> locatePorts(Context ctx, List<Host> hosts) {
        final List<Port> services = new ArrayList<>();
        for(Host host : hosts) {
            services.addAll(locatePorts(ctx, host));
        }
        return services;
    }

    default List<Port> locatePorts(Context ctx, NmapRun nmap) {
        final List<Port> ports = new ArrayList<>();
        if(nmap.getHosts() != null) {
            return locatePorts(ctx, nmap.getHosts());
        }
        return ports;
    }
    /*
        This is bollocks
     */
    default List<Target> locateExtraReasons(Context ctx, Target parent, NmapRun nmap) {
        final List<Target> ret = new ArrayList<>();
        if(nmap.getHosts() != null) {
            nmap.getHosts().forEach(host -> {
                host.getPorts().getExtraPorts().forEach(port -> {
                   port.getExtraReasons().forEach(er -> {
                       for(String range : er.getPorts().split(",")) {
                          String[] parts = range.split("-");
                          int low =  Integer.parseInt(parts[0]);
                          int high =  low;
                          if(parts.length == 2) {
                              high = Integer.parseInt(parts[1]);
                          }
                          var proto = er.getProto();
                          Reason reason = Reason.fromString(er.getReason());
                          for(int i = low; i <= high; i++) {
                              Target target = new Target();
                              Long p = Long.parseLong(i+"");
                              target.setIpAddress(parent.getIpAddress());
                              target.setPortNumber(p);
                              target.setReason(reason.name());
                              target.setPortSate(PortState.blocked.name());
                              target.setPortProtocol(proto);
                              target.setTargetType(TargetType.Service.name());
                              ret.add(target);
                          }
                       }
                   });
                });
            });

        }
        return ret;
    }

    default List<NSEScript> createScripts(Context ctx,Host host) {
        final List<Port> ports = locatePorts(ctx, host);
        final List<NSEScript> scripts = new ArrayList<>();
        if(ports != null && !ports.isEmpty()) {
            for(Port port :ports) {
                List<NSEScript> scps = createScripts(ctx, port);
                scripts.addAll(scps);
            }
        }

        return scripts;
    }
    default List<Target> createServiceTargets(Context ctx, Host host, Target parent) {
        final List<Target> services = new ArrayList<>();
        final List<Port> ports = locatePorts(ctx, host);
        if(ports != null && !ports.isEmpty()) {
            for (Port service : ports) {
                Target srv = createService(ctx, service, parent);
                if(srv != null) {
                    services.add(srv);
                }
            }
        }
        services.sort((o1, o2) -> {
            if (o1.getPortProtocol() != null && o2.getPortProtocol() != null) {
                int cmp = o1.getPortProtocol().compareTo(o2.getPortProtocol());
                if (cmp != 0) {
                    return cmp;
                }
            }
            if (o1.getPortNumber() != null && o2.getPortNumber() != null) {
                Long p1 = o1.getPortNumber();
                Long p2 = o2.getPortNumber();
                if (p1 < p2) {
                    return -1;
                }
                if (p1 > p2) {
                    return 1;
                }
            }
            if (o1.getName() != null && o2.getName() != null) {
                return o1.getName().compareTo(o2.getName());
            }
            return 0;
        });
        return services;
    }

    default List<Target> getTargets(Context ctx, Target parent, NmapRun nmap, String...serviceNames) {
        final List<Target> targets = new ArrayList<>();
        List<Port> ports = locatePorts(ctx, nmap);
        Set<String> names = new HashSet<>(Arrays.asList(serviceNames));
        for(Port port : ports) {

            Target t = createService(ctx, port, parent);
            if(names.contains(t.getName())) {
                targets.add(t);
            }
        }
        return targets;
    }

    default List<Target> getServices(Context ctx, Host host, Target target) {
        return  createServiceTargets(ctx, host, target);
    }

    default List<NSEScript> createScripts(Context ctx, Port port) {
        List<NSEScript> scripts = port.getNseScripts();
        Service service = port.getService();
        if(scripts != null && !scripts.isEmpty()) {
            for(NSEScript script : scripts) {
                script.setProtocol(port.getProtocol());
                script.setPortId(port.getPortId());
                script.setServiceName(service.getName());
            }
        }
        return port.getNseScripts();
    }
    default Target createService(Context ctx, Port port,Target parent) {
        Service service = port.getService();
        if(service != null) {
            Target srv = new Target();
            if(service.getCpes() != null) {
                Cpe cpe = service.getCpe(0);
                if(cpe != null) {
                    srv.setVersion(cpe.getVersion());
                    srv.setProduct(cpe.getProduct());
                    srv.setCpe(cpe.toCpe23FS());
                }
            }
            srv.setExtraInfo(service.getExtraInfo());
            srv.setTargetType(TargetType.Service.name());
            String ip = (parent != null) ? parent.getIpAddress()+":" : "";
            srv.setUri(ip+port.getPortId());
            srv.setIpAddress(parent.getIpAddress());
            srv.setProduct(service.getProduct());
            srv.setPortNumber(service.getPortId());
            srv.setParentUri(parent.getParentUri());
            srv.setPortSate(service.getState().getState());
            srv.setReason(port.getState().getReason());
            srv.setPortProtocol(service.getProtocol());
            srv.setName(service.getName());
            List<KnownService> kservices = getKnownServices(ctx,service.getPortId(), service.getProtocol());
            if(kservices != null) {
                StringBuilder notes = new StringBuilder();
                String desc = null;
                for(KnownService ks : kservices) {
                    if(desc == null) {
                        desc = ks.getComment();
                    }
                    String cmt = ks.getComment();
                    if(desc != null && cmt != null) {
                        notes.append(desc).append("::");
                        desc = ks.getComment();
                    }
                }
                srv.setDescription(desc);
            }
            return srv;
        }
        return null;
    }

    default NmapRun createNmapRun(Context ctx, String xml, String prov) {
        NmapXmlParser parser = new NmapXmlParser();
        try {
            NmapRun n = parser.parse(ctx, xml, prov);
            return n;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    default List<Host> getHosts(Context ctx, NmapRun scan) {
        return scan.getHosts();
    }

    default List<CVE> getCVEs(Context ctx, Host host) {
        Set<CVE> set = new HashSet<>();
            if(host.getPorts() != null) {
                host.getPorts().getPorts().forEach(port -> {
                    if(port.getNseScripts() != null) {
                        port.getNseScripts().forEach(nse -> {
                            if(nse.getCves() != null) {
                                set.addAll(nse.getCves());
                            }
                        });
                    }
                });
            }
        List<CVE> cves = new ArrayList<>(set);
        Collections.sort(cves, (o1, o2) -> {
            int cmp = 0;
            if(o1.getCveType() != null && o2.getCveType() != null) {
                cmp = o1.getCveType().compareTo(o2.getCveType());
                if(cmp != 0) return cmp;
            }
            if(o1.getCveId() != null && o2.getCveId() != null) {
                cmp = o1.getCveId().compareTo(o2.getCveId());
                if(cmp != 0) return cmp;
            }
            return 0;
        });
        return cves;
    }
    default List<Target> getProductTechs(Context ctx, Host host) {
        final List<Target> techs = new ArrayList<>();
        if(host.getPorts() != null) {
            host.getPorts().getPorts().forEach(port -> {
                if(port.getNseScripts() != null) {
                    port.getNseScripts().forEach(nse -> {
                        if(nse.getTechs() != null) {
                            nse.getTechs().forEach(product -> {
                                Target tech = convertToTech(product);

                                if(tech != null) {
                                    Address addr = mergeAddresses(host.getAddresses());
                                    tech.setMacAddress(addr.getMacAddr());
                                    tech.setIpAddress(addr.getAddr());
                                    tech.setVendor(addr.getVendor());
                                    tech.setPortNumber(port.getPortId());
                                    tech.setPortProtocol(port.getProtocol());
                                    tech.setPortSate(port.getState().getState());
                                    techs.add(tech);
                                }
                            });
                        }
                    });
                }
                });
            }
        return techs;
    }

    default Target convertToTech(Product product) {
        Target tech = new Target();
        // these are services
        tech.setProvenance(product.getProvenance().name());
        tech.setVersion(product.getVersion());
        tech.setName(product.getName());
        tech.setScore(product.getScore());
        if(product.getTargetType() != null) {
            tech.setTargetType(product.getTargetType().name());
        }
        tech.setPortNumber(product.getPortNumber());
        tech.setUnderlyingSystem(product.getOs());
        tech.setOsFamily(product.getOs());
        return tech;
    }

    default List<OS> getOperatingSystems(Context ctx, Host host) {
        final List<OS> list = new ArrayList<>();
        OS os = host.getOs();
        if(os != null) {
            list.add(os);
        }
        return list;
    }

    default Boolean hasOS(Context ctx, Host host) {
        List<OS> osses = getOperatingSystems(ctx, host);
        if(osses == null || osses.isEmpty()) {
            return false;
        }
        return true;
    }

    default Collection<Target> createCPEs(Context ctx, Host host) {
        final Collection<Target> targets = new LinkedHashSet<>();
        final Set<String> names = new HashSet<>();
            Ports ports = host.getPorts();
            if(ports != null) {
                ports.getPorts().forEach(port -> {
                    if(port != null) {
                        Service service = port.getService();
                        if(service != null) {
                            Cpe[] cpes = service.getCpes();
                            if (cpes != null && cpes.length > 0) {
                                for(Cpe cpe : cpes) {

                                    String product = service.getProduct();
                                    if (product != null && !product.isBlank() && !names.contains(product)) {
                                        names.add(product);
                                        String extraInfo = service.getExtraInfo();
                                        Target t = new Target();
                                        t.setVersion(cpe.getVersion());
                                        t.setVendor(cpe.getVendor());
                                        t.setProduct(product);
                                        t.setTargetType(TargetType.Service.name());
                                        t.setName(product);
                                        t.setCpe(cpe.toCpe23FS());
                                        t.setPortSate(port.getState().getState());
                                        t.setPortProtocol(port.getProtocol());
                                        t.setPortNumber(port.getPortId());
                                        t.setExtraInfo(extraInfo);
                                        targets.add(t);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        return targets;
    }
    default Collection<Target> createOperatingSystems(Context ctx, NmapRun scan) {
        final Collection<Target> targets = new LinkedHashSet<>();
        final Set<String> names = new HashSet<>();
        for(Host host : getHosts(ctx, scan)) {
            OS os = host.getOs();
            if(os != null) {
                if(os.getOsMatches() != null) {
                    os.getOsMatches().forEach(osm -> {
                        if(osm != null) {
                            if(osm.getOsClasses() != null) {
                                osm.getOsClasses().forEach(osc -> {
                                    if(!names.contains(osm.getName())) {
                                        names.add(osm.getName());
                                        Target t = new Target();
                                        t.setTargetType(TargetType.OperatingSystem.name());
                                        t.setName(osm.getName());
                                        t.setVendor(osc.getVendor());
                                        t.setOsFamily(osc.getOsFamily());
                                        t.setOsGen(osc.getOsGen());
                                        t.setOsAccuracy(osc.getAccuracy());
                                        targets.add(t);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }
        return targets;
    }

    default Address mergeAddresses(List<Address> addresses) {
        Address addr = new Address();
        for(Address address : addresses) {
            if(address.getAddrType().equals("ipv4")) {
                addr.setAddr(address.getAddr());
                addr.setAddrType(address.getAddrType());
            } else {
                addr.setMacAddr(address.getAddr());
                addr.setVendor(address.getVendor());
            }
        }
        return addr;
    }
    default Target getOperatingSystem(Context ctx, Host host) {
        if(!hasOS(ctx, host)) {
            return null;
        }
        Target target = new Target();

            Address addr = mergeAddresses(host.getAddresses());
            if (addr != null) {
                target.setIpAddress(addr.getAddr());
                target.setMacAddress(addr.getMacAddr());
                target.setVendor(addr.getVendor());
                target.setName(addr.getVendor());
            }
            OS os = host.getOs();

            if (os != null) {
                OSMatch osMatched = null;
                OSClass osClassMatched = null;

                List<OSMatch> matches = os.getOsMatches();
                if (matches != null && !matches.isEmpty()) {
                    for (OSMatch match : matches) {
                        if (osMatched == null) {
                            osMatched = match;
                        }
                        Long accuracy = match.getAccuracy();
                        if (accuracy > osMatched.getAccuracy()) {
                            osMatched = match;
                        }
                        List<OSClass> osClasses = match.getOsClasses();
                        for (OSClass osc : osClasses) {
                            if (osClassMatched == null) {
                                osClassMatched = osc;
                            }
                            if(osClassMatched != null) {
                                if (osc.getAccuracy() > osClassMatched.getAccuracy()) {
                                    osClassMatched = osc;
                                }
                            }
                        }
                    }
                    Long accuracy = osClassMatched.getAccuracy();
                    if (accuracy == null) {
                        accuracy = 0l;
                    }
                    target.setOsAccuracy(accuracy);
                    // This might be better promoted to the layer name
                    TargetType tt = getTargetType(osClassMatched);//TargetType.fromString(osClassMatched.getType());
                    if(tt != null && !TargetType.Unknown.equals(tt)) {
                        target.setTargetType(tt.name());
                    }
                    target.setOsType(osClassMatched.getType());
                    target.setOsFamily(osClassMatched.getOsFamily());
                    target.setOsGen(osClassMatched.getOsGen());
                    target.setOsVendor(osClassMatched.getVendor());
                    List<Cpe> cpes = osClassMatched.getCpes();
                    for (Cpe cpe : cpes) {
                        if(target.getTargetType() == null) {
                            switch (cpe.getPart()) {
                                case OPERATING_SYSTEM -> target.setTargetType(TargetType.OperatingSystem.name());
                                case ANY -> target.setTargetType(TargetType.Any.name());
                                case HARDWARE_DEVICE -> target.setTargetType(TargetType.Device.name());
                                case NA -> target.setTargetType(TargetType.NotApplicable.name());
                                case APPLICATION -> target.setTargetType(TargetType.Application.name());
                            }
                        }
                        //layer.setName(cpe.toString());
                        target.setName(cpe.getVendor());
                        target.setUri(target.getIpAddress());
                        target.setCpe(cpe.toCpe23FS());
                        target.setProduct(cpe.getProduct());
                        target.setProvenance(Provenance.NMap.name());
                    }
                }
        }
        return target;
    }

    default TargetType getTargetType(OSClass osClass) {
        TargetType tt = TargetType.fromString(osClass.getType());
        if(tt == null || TargetType.Unknown.equals(tt)) {
            if(osClass.getCpes() != null && !osClass.getCpes().isEmpty()) {
                Cpe cpe = osClass.getCpes().get(0);
                return switch (cpe.getPart()) {
                    case OPERATING_SYSTEM -> TargetType.OperatingSystem;
                    case NA -> TargetType.NotApplicable;
                    case ANY -> TargetType.Any;
                    case APPLICATION -> TargetType.Application;
                    case HARDWARE_DEVICE -> TargetType.Device;
                };
            }
        }
        return tt;
    }
    default List<OSMatch> getOSMatches(Context ctx, Host host) {
        final List<OSMatch> matches = new ArrayList<>();
        OS os = host.getOs();
        if(os != null) {
            matches.addAll(os.getOsMatches());
        }
        return matches;
    }
}
