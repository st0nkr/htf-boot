package com.teto.domain.scan;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;

import java.util.Date;

public class Scan  {
    @Meta(tag = Tag.ID, id = true)
    private Integer id;
    @Meta(tag = Tag.TargetId, notnull = true)
    private Integer targetId;
    @Meta(tag = Tag.Name)
    private String name;
    @Meta(tag = Tag.Description)
    private String description;
    @Meta(tag = Tag.ScanType)
    private String scanType;
    @Meta(tag = Tag.NextScanDate)
    private String nextScanDate;
    @Meta(ignore = true)
    private transient String displayName;
    @Meta(tag = Tag.StartDate, index = 4)
    protected Date startDate;
    @Meta(tag = Tag.EndDate, index = 5)
    protected Date endDate;

    public String getNextScanDate() {
        return nextScanDate;
    }

    public void setNextScanDate(String nextScanDate) {
        this.nextScanDate = nextScanDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    public void setDisplayName(String displayName) {
        if(displayName == null) {
            displayName = name;
        }
        this.displayName = displayName;
    }
}
