package com.teto;

import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;

import java.util.*;

public interface IAttackVector {

    default boolean hasWebServer(Context ctx, TargetNode node) {
        if(hasService(ctx, node, node.getTarget().getIpAddress(), 80L, TargetType.Service) ||
            hasService(ctx, node, node.getTarget().getIpAddress(), 443L, TargetType.Service)) {
            return true;
        }
        return false;
    }

    default String getWebServerUrl(Context ctx, TargetNode node) {
        if(hasService(ctx, node, node.getTarget().getIpAddress(), 80L, TargetType.Service)) {
            return "http://"+node.getTarget().getIpAddress();
        }
        if(hasService(ctx, node, node.getTarget().getIpAddress(), 443L, TargetType.Service)) {
            return "https://"+node.getTarget().getIpAddress();
        }
        return null;
    }
    default boolean hasSamePorts(Context ctx, TargetNode node) {
        final Map<Long, List<Target>> tcpPorts = new HashMap<>();
        final Map<Long, List<Target>> udpPorts = new HashMap<>();
        for(Target t : node.getScannedTargets().getTargets()) {
            if(t.getPortNumber() != null) {
                Map<Long, List<Target>> map = null;
                if("tcp".equals(t.getPortProtocol())){
                    map = tcpPorts;
                }
                if("udp".equals(t.getPortProtocol())) {
                    map = udpPorts;
                }
                if(map != null) {
                    map.computeIfAbsent(t.getPortNumber(), k -> new ArrayList<>()).add(t);
                }
            }
        }
        for(Long key : tcpPorts.keySet()) {
            if(tcpPorts.get(key).size() > 1)  {
                return true;
            }
        }
        for(Long key : udpPorts.keySet()) {
            if(udpPorts.get(key).size() > 1)  {
                return true;
            }
        }
        return false;
    }
    default boolean hasService(Context ctx, TargetNode node, String ip, Long port, TargetType tt) {
        for(Target target : node.getScannedTargets().getTargets()) {
            TargetType tt2 = TargetType.fromString(target.getTargetType());
            String tip = target.getIpAddress();
            Long tpn = target.getPortNumber();
            if(ip.equals(tip) && Objects.equals(tpn, port) && tt.equals(tt2)) {
                return true;
            }
        }
        return false;
    }
    default List<Target> getHTTPTargets(Context ctx, TargetNode node, String ip) {
        final List<Target> http = new ArrayList<>();
        for(Target target : node.getScannedTargets().getTargets()) {
            TargetType tt = TargetType.fromString(target.getTargetType());
            String tip = target.getIpAddress();
            if(ip.equals(tip) && TargetType.Service.equals(tt)) {
                if("http".equalsIgnoreCase(target.getDescription())) {
                    http.add(target);
                }
            }
        }
        return http;
    }

    default List<Target> getHTTPSTargets(Context ctx, TargetNode node, String ip) {
        final List<Target> http = new ArrayList<>();
        for(Target target : node.getScannedTargets().getTargets()) {
            TargetType tt = TargetType.fromString(target.getTargetType());
            String tip = target.getIpAddress();
            if(ip.equals(tip) && TargetType.Service.equals(tt)) {
                if("https".equalsIgnoreCase(target.getDescription())) {
                    http.add(target);
                }
            }
        }
        return http;
    }

    default List<Target> getHTTPTargets(Context ctx, TargetNode node) {
        return getTargets(ctx, node, TargetType.Service, 80);
    }

    default List<Target> getHTTPTSTargets(Context ctx, TargetNode node) {
        return getTargets(ctx, node, TargetType.Service, 443);
    }

    default List<Target> getTargets(Context ctx, TargetNode node, TargetType tt, long port) {
        final Collection<Target> targets = new HashSet<>();
        for(Target target : node.getScannedTargets().getTargets()) {
            TargetType tt2 = TargetType.fromString(target.getTargetType());
            Long tpn = target.getPortNumber();
            if(Objects.equals(tpn, port) && tt.equals(tt2)) {
                targets.add(target);
            }
        }
        return new ArrayList<>(targets);
    }

}
