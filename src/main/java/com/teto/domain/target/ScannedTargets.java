package com.teto.domain.target;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.Answer;
import com.teto.domain.api.ApiEndPoint;
import com.teto.domain.cloud.StorageBucket;
import com.teto.domain.contact.ContactDetails;
import com.teto.domain.cve.CVE;
import com.teto.domain.difficulty.Difficulty;
import com.teto.domain.directory.Directory;
import com.teto.domain.email.EMail;
import com.teto.domain.exploit.Exploit;
import com.teto.domain.header.Header;
import com.teto.domain.ipaddress.Ipv4;
import com.teto.domain.ipaddress.Ipv6;
import com.teto.domain.mx.MailExchange;
import com.teto.domain.nameserver.NameServer;
import com.teto.domain.pbx.PBX;
import com.teto.domain.port.ServicePort;
import com.teto.domain.subdomain.SubDomain;
import com.teto.domain.swagger.Swagger;
import com.teto.domain.url.Url;
import com.teto.domain.user.ScannedUser;
import com.teto.domain.user.SocialMediaUser;
import com.teto.domain.waf.WAF;
import com.teto.domain.web.form.PageForm;
import com.teto.domain.wordlist.WordList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class ScannedTargets {
    private List<ContactDetails> contactDetails = new ArrayList<>();
    private Collection<Target> targets = new TreeSet<>();
    private Collection<Header> headers = new TreeSet<>();
    private Collection<CVE> cves = new TreeSet<>();
    private Collection<Exploit> exploits = new TreeSet<>();
    private Collection<WAF> wafs = new TreeSet<>();
    private Collection<SubDomain> subDomains = new TreeSet<>();
    private Collection<MailExchange> mailExchanges = new TreeSet<>();
    private Collection<NameServer> nameServers = new TreeSet<>();
    private Collection<ScannedUser> users = new TreeSet<>();
    private Collection<EMail> emails = new TreeSet<>();
    private Collection<Swagger> swaggers = new TreeSet<>();
    private Collection<ServicePort> servicePorts = new TreeSet<>();
    private Collection<Ipv4> ipv4s = new TreeSet<>();
    private Collection<Ipv6> ipv6s = new TreeSet<>();
    private Collection<Directory> directories = new TreeSet<>();
    private Collection<ApiEndPoint> apiEndPoints = new TreeSet<>();
    private Collection<PBX> pbxs = new TreeSet<>();
    private Collection<WordList> wordLists = new TreeSet<>();
    private Collection<PageForm> pageForms = new TreeSet<>();
    private Collection<Url> urls = new TreeSet<>();
    private Collection<StorageBucket> storageBuckets = new TreeSet<>();
    private Collection<SocialMediaUser> socialMediaUsers = new TreeSet<>();
    private Answer loadBalanced;
    private String status;
    private String fileName;

    public void add(ScannedTargets targs) {
        if(targs != null) {
            if(targs.getStorageBuckets() != null) {
                getStorageBuckets().addAll(targs.getStorageBuckets());
            }
            if(targs.getSocialMediaUsers() != null) {
                getSocialMediaUsers().addAll(targs.getSocialMediaUsers());
            }
            if(targs.getUrls() != null) {
                getUrls().addAll(targs.getUrls());
            }
            if(targs.getApiEndPoints() != null) {
                getApiEndPoints().addAll(targs.getApiEndPoints());
            }
            if(targs.getPbxs() != null) {
                getPbxs().addAll(targs.getPbxs());
            }
            if(targs.getLoadBalanced() != null) {
                setLoadBalanced(targs.getLoadBalanced());
            }
            if(targs.getMailExchanges() != null) {
                getMailExchanges().addAll(targs.getMailExchanges());
            }
            if(targs.getNameServers() != null) {
                getNameServers().addAll(targs.getNameServers());
            }
            if(targs.getDirectories() != null) {
                getDirectories().addAll(targs.getDirectories());
            }
            if(targs.getIpv4s() != null) {
                getIpv4s().addAll(targs.getIpv4s());
            }

            if(targs.getIpv6s() != null) {
                getIpv6s().addAll(targs.getIpv6s());
            }
            if(targs.getServicePorts() != null) {
                getServicePorts().addAll(targs.getServicePorts());
            }
            if(targs.getUsers() != null) {
                getUsers().addAll(targs.getUsers());
            }
            if(targs.getEmails() != null) {
                getEmails().addAll(targs.getEmails());
            }
            if(targs.getSwaggers() != null) {
                getSwaggers().addAll(targs.getSwaggers());
            }
            for (Target t : targs.getTargets()) {
                if(!isEmpty(t.getUri())) {
                    if (!targets.contains(t)) {
                        targets.add(t);
                    }
                }
            }
            if(targs.getSubDomains() != null) {
                getSubDomains().addAll(targs.getSubDomains());
            }
            if(targs.getServicePorts() != null) {
                getServicePorts().addAll(targs.getServicePorts());
            }
            if(targs.getWafs() != null) {
                getWafs().addAll(targs.getWafs());
            }
            if(targs.getHeaders() != null) {
                getHeaders().addAll(targs.getHeaders());
            }
            if(targs.getCves() != null) {
                getCves().addAll(targs.getCves());
            }
            if(targs.getExploits() != null) {
                getExploits().addAll(targs.getExploits());
            }
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.isBlank();
    }


    public List<Target> getOperatingSystems() {
        final List<Target> osses = new ArrayList<>();
        getTargets().forEach(t -> {
            TargetType tt = TargetType.fromString(t.getTargetType());
            if(TargetType.OperatingSystem.equals(tt)) {
                osses.add(t);
            }
        });
        return osses;
    }
}
