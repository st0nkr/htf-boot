package com.teto;

import com.teto.command.Context;
import com.teto.command.regex.ExtractTargetTypes;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.*;

public interface ITargetBuilder extends ILogger, IJSON, IIPAddresses {
    default Collection<Target> buildTargets(Context ctx, boolean verify, TargetType tt, Collection<String> strings) {
        final Collection<Target> targs = new ArrayList<>();
        for(String str : strings) {
            if(str.isBlank()) {
                continue;
            }
            str = str.toLowerCase();
            Target t = Target.create(tt.name(), str);
            t.setName(tt.name());
            t.setUri( str);
            t.setTargetType(tt.name());
            if(str.startsWith("ftp.") || str.startsWith("ftps.")) {
                t.setTargetType(TargetType.Ftp.name());
            }
            if(str.startsWith("mx") || str.startsWith("mail")) {
                t.setTargetType(TargetType.MailExchange.name());
            }
            if(str.startsWith("ns")) {
                t.setTargetType(TargetType.NameServer.name());
            }
            if(str.startsWith("smtp")) {
                t.setTargetType(TargetType.Smtp.name());
            }
            targs.add(t);
        }
        return targs;
    }

    default Collection<Target> buildRESTTargets(Context ctx, boolean verify, Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.REST, strings);
    }

    default Collection<Target> buildSOAPTargets(Context ctx, boolean verify, Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.SOAP, strings);
    }
    default Collection<Target> buildTelnetTargets(Context ctx, boolean verify, Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.Telnet, strings);
    }
    default Collection<Target> buildHostTargets(Context ctx,boolean verify,Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.Host, strings);
    }
    default Collection<Target> buildSSHTargets(Context ctx,boolean verify,Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.SSH, strings);
    }
    default Collection<Target> buildCPETargets(Context ctx, boolean verify, Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.CPE, strings);
    }

    default Collection<Target> buildEmails(Context ctx, boolean verify, Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.Email, strings);
    }

    default Collection<Target> buildPOP3Targets(Context ctx, boolean verify, Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.POP3, strings);
    }
    default Collection<Target> buildDeviceTargets(Context ctx,boolean verify,Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.Device, strings);
    }

    default Collection<Target> buildFirewallTargets(Context ctx,boolean verify,Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.Firewall, strings);
    }

    default Collection<Target> buildNFSTargets(Context ctx,boolean verify,Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.NFS, strings);
    }

    default Collection<Target> buildFTPTargets(Context ctx,boolean verify,Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.Ftp, strings);
    }
    default Collection<Target> buildWebSocketTargets(Context ctx,boolean verify,Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.WebSocket, strings);
    }

    default Collection<Target> buildUrlTargets(Context ctx,boolean verify,Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.Url, strings);
    }
    default Collection<Target> buildSAMBATargets(Context ctx,boolean verify,Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.SAMBA, strings);
    }
    default Collection<Target> buildSmtpTargets(Context ctx,boolean verify,Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.Smtp, strings);
    }

    default Collection<Target> buildDatabaseTargets(Context ctx,boolean verify,Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.Database, strings);
    }

    default Collection<Target> buildOSTargets(Context ctx,boolean verify,Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.OperatingSystem, strings);
    }

    default Collection<Target> buildJdbcTargets(Context ctx,boolean verify,Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.Jdbc, strings);
    }

    default Collection<Target> buildIPV6Targets(Context ctx,boolean verify, Collection<String> strings) {
        final Collection<String> ips = new TreeSet<>();
        for(String str : strings) {
            if(verify) {
                Target fx = Target.create(null, str);
                fx.setTargetType(TargetType.Ipv6.name());
                ips.add(str);
            } else {
                ips.add(str);
            }
        }
        return buildTargets(ctx, verify, TargetType.Ipv6, ips);
    }


    default Collection<Target> buildMailExchangeTargets(Context ctx,boolean verify, Collection<String> strings) {
        return buildTargets(ctx, verify,TargetType.MailExchange, strings);
    }

    default Collection<Target> buildNameServerTargets(Context ctx,boolean verify, Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.NameServer, strings);
    }

    default Collection<Target> buildWebTargets(Context ctx,boolean verify, Collection<String> strings) {
        return buildTargets(ctx, verify, TargetType.Web, strings);
    }

    default Collection<Target> buildDomainTargets(Context ctx,boolean verify, Collection<String> strings) {
        final Collection<String> domains = new TreeSet<>();
        for(String str : strings) {
            if(!isDomain(str)) {
                continue;
            }
            if(verify) {
                Target fx = new Target();
                fx.setUri(str);
                fx.setTargetType(TargetType.SubDomain.name());
                domains.add(str);
            } else {
                domains.add(str);
            }
        }
        return buildTargets(ctx, verify, TargetType.SubDomain, domains);
    }

    default Collection<Target> buildIPV4Targets(Context ctx,boolean verify, Collection<String> strings) {
        final Collection<String> ips = new TreeSet<>();
        for(String str : strings) {
            if(verify) {
                Target fx = new Target();
                fx.setUri(str);
                fx.setTargetType(TargetType.Ipv4.name());
                ips.add(str);
            } else {
                ips.add(str);
            }
        }
        return buildTargets(ctx, verify, TargetType.Ipv4, ips);
    }

    default Collection<Target> buildCIDRTargets(Context ctx,boolean verify, Collection<String> strings) {

        final Collection<Target> ips = new TreeSet<>();
        for(String ip : strings) {
            Target t = new Target();
            t.setTargetType(TargetType.CIDR.name());
            t.setUri(ip);
            ips.add(t);
        }
        return ips;
    }

    default Collection<Target> extractTargets(Context ctx, boolean verify, String str) {
        final Map<TargetType, Collection<String>> targs = extractTargetTypes(ctx, str);
        Collection<Target> total = new TreeSet<>();
        for(TargetType tt: targs.keySet()) {
            Collection<Target> targets = null;
            switch (tt) {
                case Ipv4 -> targets = buildIPV4Targets(ctx, verify, targs.get(tt));
                case Ipv6 -> targets = buildIPV6Targets(ctx, verify, targs.get(tt));
                case NameServer -> targets = buildNameServerTargets(ctx, verify, targs.get(tt));
                case MailExchange -> targets = buildMailExchangeTargets(ctx, verify, targs.get(tt));
                case Domain -> targets = buildDomainTargets(ctx, verify, targs.get(tt));
                case Host -> targets = buildHostTargets(ctx, verify, targs.get(tt));
                case SSH -> targets = buildSSHTargets(ctx, verify, targs.get(tt));
                case Device -> targets = buildDeviceTargets(ctx, verify, targs.get(tt));
                case REST -> targets = buildRESTTargets(ctx, verify, targs.get(tt));
                case Database -> targets = buildDatabaseTargets(ctx, verify, targs.get(tt));
                case Jdbc -> targets = buildJdbcTargets(ctx, verify, targs.get(tt));
                case CIDR -> targets = buildCIDRTargets(ctx, verify, targs.get(tt));
                case Firewall -> targets = buildFirewallTargets(ctx, verify, targs.get(tt));
                case NFS -> targets = buildNFSTargets(ctx, verify, targs.get(tt));
                case WebSocket -> targets = buildWebSocketTargets(ctx, verify, targs.get(tt));
                case Ftp -> targets = buildFTPTargets(ctx, verify, targs.get(tt));
                case Telnet -> targets = buildTelnetTargets(ctx, verify, targs.get(tt));
                case SOAP -> targets = buildSOAPTargets(ctx, verify, targs.get(tt));
                case Smtp -> targets = buildSmtpTargets(ctx, verify, targs.get(tt));
                case Web -> targets = buildWebTargets(ctx, verify, targs.get(tt));
                case SAMBA -> targets = buildSAMBATargets(ctx, verify, targs.get(tt));
                case JSON -> targets = extractJsonTargets(ctx,verify,targs.get(tt));
                case Url ->targets = buildUrlTargets(ctx, verify, targs.get(tt));
                default -> error(this,"TargetType not supported: "+tt);
            }
            if(targets != null) {
                total.addAll(targets);
            }
        }
        return total;
    }

    default Collection<Target> extractJsonTargets(Context ctx, boolean verify, Collection<String> jsons) {
        final Collection<Target> targets = new LinkedHashSet<>();
        for(String json : jsons) {
            try {
                json = json.replace("'","\"");
                Optional<Object> obj = fromJson(json, Object.class);
                if(obj.isPresent()) {
                    if(obj.get() instanceof LinkedHashMap) {
                        LinkedHashMap lhm = (LinkedHashMap) obj.get();
                        lhm.forEach((k,v) -> {
                            String key = (String) k;
                            String value = (String) v;
                            if(isValidIPV4(key)) {
                                Collection<Target> ipv4s = buildIPV4Targets(ctx, verify, Arrays.asList(key));
                                targets.addAll(ipv4s);
                                if(isDomain(value)) {
                                    Collection<Target> domains = buildDomainTargets(ctx, verify, Arrays.asList(value));
                                    targets.addAll(domains);
                                }
                            }
                            if(isValidIPV6(key)) {
                                Collection<Target> ipv6s = buildIPV6Targets(ctx, verify, Arrays.asList(key));
                                targets.addAll(ipv6s);
                                if(isDomain(value)) {
                                    Collection<Target> domains = buildDomainTargets(ctx, verify, Arrays.asList(value));
                                    targets.addAll(domains);
                                }
                            }
                        });
                    }
                }
            } catch (Exception e) {
                error(this, "Failed to parse JSON target: "+json, e);
            }
        }

        return targets;
    }

    default Map<TargetType, Collection<String>> extractTargetTypes(Context ctx, String str) {
        Optional<Map<TargetType, Collection<String>>> matches = ctx.apply(new ExtractTargetTypes(str));
        return matches.orElse(null);
    }
}
