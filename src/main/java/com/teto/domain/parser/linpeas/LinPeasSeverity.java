package com.teto.domain.parser.linpeas;

public enum LinPeasSeverity {
    RED_YELLOW("CRITICAL", "95% a PE vector"),
    RED("HIGH", "Interesting / Look into it"),
    YELLOW("MEDIUM", "Warning / High interest"),
    CYAN("LOW", "Users with console / Interesting"),
    BLUE("INFO", "Users without console & mounted devs"),
    GREEN("INFO", "Common thing"),
    MAGENTA("INFO", "Current user"),
    INFO("INFO", "Information");

    private final String level;
    private final String description;

    LinPeasSeverity(String level, String description) {
        this.level = level;
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }
}
