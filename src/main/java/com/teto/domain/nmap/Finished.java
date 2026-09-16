package com.teto.domain.nmap;

import com.teto.domain.BaseEntity;
public class Finished extends BaseEntity {
    private final Long time;
    private final String timeStr;
    private final String summary;
    private final String elapsed;
    private final String exit;

    public Finished(Long time, String timeStr, String summary, String elapsed, String exit) {
        this.time = time;
        this.timeStr = timeStr;
        this.summary = summary;
        this.elapsed = elapsed;
        this.exit = exit;
    }

    public Long getTime() {
        return time;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public String getSummary() {
        return summary;
    }

    public String getElapsed() {
        return elapsed;
    }

    public String getExit() {
        return exit;
    }
}
