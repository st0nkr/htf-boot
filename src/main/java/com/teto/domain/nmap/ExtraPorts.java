package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;

import java.util.List;
public class ExtraPorts extends BaseEntity {
    private final String filtered;
    private final Long count;
    private final List<ExtraReasons> extraReasons;

    public ExtraPorts(String filtered, Long count, List<ExtraReasons> extraReasons) {
        this.filtered = filtered;
        this.count = count;
        this.extraReasons = extraReasons;
    }

    public String getFiltered() {
        return filtered;
    }

    public Long getCount() {
        return count;
    }

    public List<ExtraReasons> getExtraReasons() {
        return extraReasons;
    }
}
