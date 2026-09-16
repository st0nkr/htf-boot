package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;
import us.springett.parsers.cpe.Cpe;

import java.util.List;

public class OSClass extends BaseEntity {
    private final String type;
    private final String vendor;
    private final String osFamily;
    private final String osGen;
    private final Long accuracy;
    private final List<Cpe> cpes;

    public OSClass(String type, String vendor, String osFamily, String osGen, Long accuracy, List<Cpe> cpes) {
        this.type = type;
        this.vendor = vendor;
        this.osFamily = osFamily;
        this.osGen = osGen;
        this.accuracy = accuracy;
        this.cpes = cpes;
    }

    public String getType() {
        return type;
    }

    public String getVendor() {
        return vendor;
    }

    public String getOsFamily() {
        return osFamily;
    }

    public String getOsGen() {
        return osGen;
    }

    public Long getAccuracy() {
        return accuracy;
    }

    public List<Cpe> getCpes() {
        return cpes;
    }
}
