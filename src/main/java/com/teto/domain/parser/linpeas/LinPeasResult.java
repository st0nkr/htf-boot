package com.teto.domain.parser.linpeas;

import com.teto.domain.cve.CVE;
import com.teto.domain.exploit.Exploit;
import com.teto.domain.port.ServicePort;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;
import com.teto.domain.user.ScannedUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class LinPeasResult {
    private String os;
    private String osFamily = "Linux";
    private String osName;
    private String osVersion;
    private String kernel;
    private String architecture;
    private String hostname;

    private String currentUser;
    private Integer currentUid;
    private Integer currentGid;
    private String currentGroup;
    private List<String> groups = new ArrayList<>();

    private String sudoVersion;
    private List<String> sudoRules = new ArrayList<>();

    private List<String> suidFiles = new ArrayList<>();
    private List<String> sgidFiles = new ArrayList<>();

    private List<String> cronJobs = new ArrayList<>();
    private List<String> timers = new ArrayList<>();

    private List<String> listeningPorts = new ArrayList<>();
    private List<ServicePort> servicePorts = new ArrayList<>();

    private List<String> networkInterfaces = new ArrayList<>();
    private List<String> ipAddresses = new ArrayList<>();

    private List<String> usersWithConsole = new ArrayList<>();
    private List<ScannedUser> users = new ArrayList<>();

    private List<String> passwordHashes = new ArrayList<>();
    private List<String> passwords = new ArrayList<>();
    private List<String> credentials = new ArrayList<>();
    private List<String> sshKeys = new ArrayList<>();

    private List<CVE> cves = new ArrayList<>();
    private List<Exploit> exploits = new ArrayList<>();

    private String cloudProvider;
    private String containerType;

    private List<String> writableFiles = new ArrayList<>();
    private List<String> writableDirectories = new ArrayList<>();

    private Map<String, String> environmentVariables = new LinkedHashMap<>();

    private List<LinPeasSection> sections = new ArrayList<>();
    private List<LinPeasFinding> findings = new ArrayList<>();

    public List<LinPeasFinding> getFindingsBySeverity(LinPeasSeverity severity) {
        if (severity == null || findings == null) {
            return Collections.emptyList();
        }
        return findings.stream()
                .filter(f -> severity.equals(f.getSeverity()))
                .collect(Collectors.toList());
    }

    public List<LinPeasFinding> getCriticalFindings() {
        return getFindingsBySeverity(LinPeasSeverity.RED_YELLOW);
    }

    public List<LinPeasFinding> getHighFindings() {
        return getFindingsBySeverity(LinPeasSeverity.RED);
    }

    public LinPeasSection findSection(String sectionNameKeyword) {
        if (sectionNameKeyword == null || sections == null) {
            return null;
        }
        for (LinPeasSection sec : sections) {
            if (sec.getName() != null && sec.getName().toLowerCase().contains(sectionNameKeyword.toLowerCase())) {
                return sec;
            }
        }
        return null;
    }

    public ScannedTargets toScannedTargets(Target parent) {
        ScannedTargets st = new ScannedTargets();
        Long parentId = parent != null ? parent.getId() : null;
        String parentType = parent != null ? parent.getTargetType() : null;
        int parentLevel = parent != null && parent.getLevel() != null ? parent.getLevel() : 0;

        if (os != null || kernel != null || hostname != null) {
            Target osTarget = new Target();
            osTarget.setParentId(parentId);
            osTarget.setParentType(parentType);
            osTarget.setLevel(parentLevel + 1);
            osTarget.setTargetType(TargetType.OperatingSystem.name());
            osTarget.setProvenance(Provenance.LinPeas.name());
            osTarget.setName(osName != null ? osName : (os != null ? os : "Linux"));
            osTarget.setUnderlyingSystem(os != null ? os : kernel);
            osTarget.setOsFamily(osFamily != null ? osFamily : "Linux");
            osTarget.setOsVersion(osVersion != null ? osVersion : kernel);
            if (hostname != null) {
                osTarget.setUri(hostname);
            }
            if (ipAddresses != null && !ipAddresses.isEmpty()) {
                osTarget.setIpAddress(ipAddresses.get(0));
            }
            st.getTargets().add(osTarget);
        }

        if (cves != null) {
            for (CVE cve : cves) {
                if (cve.getParentId() == null) cve.setParentId(parentId);
                if (cve.getParentType() == null) cve.setParentType(parentType);
                if (cve.getLevel() == null) cve.setLevel(parentLevel + 1);
                if (cve.getProvenance() == null) cve.setProvenance(Provenance.LinPeas);
                st.getCves().add(cve);
            }
        }

        if (exploits != null) {
            for (Exploit exploit : exploits) {
                if (exploit.getTargetId() == null) exploit.setTargetId(parentId);
                if (exploit.getProvenance() == null) exploit.setProvenance(Provenance.LinPeas.name());
                st.getExploits().add(exploit);
            }
        }

        if (users != null) {
            for (ScannedUser user : users) {
                if (user.getParentId() == null) user.setParentId(parentId);
                if (user.getParentType() == null) user.setParentType(parentType);
                if (user.getLevel() == null) user.setLevel(parentLevel + 1);
                if (user.getProvenance() == null) user.setProvenance(Provenance.LinPeas.name());
                st.getUsers().add(user);
            }
        }

        if (servicePorts != null) {
            for (ServicePort sp : servicePorts) {
                if (sp.getParentId() == null) sp.setParentId(parentId);
                if (sp.getParentType() == null) sp.setParentType(parentType);
                if (sp.getLevel() == null) sp.setLevel(parentLevel + 1);
                if (sp.getProvenance() == null) sp.setProvenance(Provenance.LinPeas.name());
                st.getServicePorts().add(sp);
            }
        }

        return st;
    }
}
