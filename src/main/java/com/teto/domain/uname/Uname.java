package com.teto.domain.uname;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Uname {
    String kernalName;
    String nodeName;
    String kernelRelease;
    String kernelVersion;
    String machineName;
    String processors;
    String hardwarePlatform;
    String os;

    @Override
    public String toString() {
        return "Uname{" +
                "kernalName='" + kernalName + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", kernelRelease='" + kernelRelease + '\'' +
                ", kernelVersion='" + kernelVersion + '\'' +
                ", machineName='" + machineName + '\'' +
                ", processors='" + processors + '\'' +
                ", hardwarePlatform='" + hardwarePlatform + '\'' +
                ", os='" + os + '\'' +
                '}';
    }
}
