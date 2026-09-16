package com.teto.domain.script;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Setter
@Getter
public class Script implements Comparable<Script> {
    @JsonIgnore
    @Meta(tag = Tag.ID, id = true, index = 0)
    private Integer id;
    @Meta(tag = Tag.Name, notnull = true, index = 1, unique = true)
    private String name;
    @Meta(tag = Tag.Skip, index = 1)
    private Boolean skip = false;
    @Meta(tag = Tag.ScriptExecutable,  notnull = true, index = 2)
    private String executable;
    @Meta(tag = Tag.Prefix)
    private String prefixCommand;
    @Meta(tag = Tag.ScriptCommandToDisplayHelp, index = 3)
    private String helpCommand;
    @Meta(tag = Tag.ScriptUpdateCommand, index = 3)
    private String updateCommand;
    @Meta(tag = Tag.Categories, index = 4)
    private String categories;
    @Meta(tag = Tag.NoHup, index = 5)
    private Boolean nohup = false;
    @Meta(tag = Tag.Sudo, index = 5)
    private Boolean sudo = false;
    @Meta(tag = Tag.ProxyChains, index = 6)
    private Boolean proxyChains = true;
    @Meta(tag = Tag.CommandLine, index = 7)
    private String commandLine;
    @Meta(tag = Tag.NormalCommandLine, index = 7)
    private String normalCommandLine;
    @Meta(tag = Tag.KeyWords, index = 8)
    private String keyWords;
    @Meta(tag = Tag.Description, index = 9)
    private String description;
    @JsonIgnore
    @Meta(tag = Tag.Icon, index = 10)
    private String icon;
    @Meta(tag = Tag.OutputFormat, index = 11)
    private String outputFormat;
    @Meta(tag = Tag.StdErr)
    private String stdErr = "/dev/null";
    @JsonIgnore
    @Meta(ignore = true)
    private transient String displayName;
    @Meta(tag = Tag.DetectionProbability, index = 12)
    private Double detectionProbability;
    @Meta(tag = Tag.AdditionalArgs, index = 13)
    private String additionalArgs;
    @Meta(tag = Tag.Example1, index = 14)
    private String example1;
    @Meta(tag = Tag.Example2, index = 15)
    private String example2;
    @Meta(tag = Tag.Example3, index = 16)
    private String example3;
    @Meta(tag = Tag.Example4, index = 17)
    private String example4;
    @Meta(tag = Tag.MinExecutionDuration, index = 19)
    private Long minExecutionDuration;
    @Meta(tag = Tag.MaxExecutionDuration, index = 20)
    private Long maxExecutionDuration;
    @Meta(tag = Tag.AvgExecutionDuration, index = 21)
    private Float avgExecutionDuration;
    @Meta(tag = Tag.RunCount, index = 22)
    private Integer runCount = 0;
    @Meta(tag = Tag.Error, index = 23)
    private String lastError;
    @Meta(tag = Tag.LastCommand, index = 24)
    private String lastCommand;
    @Meta(tag = Tag.MainCategory, index = 24)
    private String mainCategory;
    @Meta(tag = Tag.SubCategory, index = 25)
    private String subCategory;
    @Meta(tag = Tag.Priority, index = 26)
    private Integer priority = 0;
    @Meta(tag = Tag.RequiresTerminal, index = 26)
    private Boolean requireTerminal = false;
    @Meta(tag = Tag.Repository, index = 27)
    private String repository;
    @Meta(tag = Tag.BuildCommand, index = 28)
    private String buildCommand;
    @Meta(tag = Tag.BuildCommand, index = 29)
    private Long lastUpdate;
    private Map<String, Integer> yield = new HashMap<>();


    public Script(String name, String executable, String help, String cats) {
        this.name = name;
        this.executable = executable;
        this.helpCommand = help;
        this.categories = cats;
    }

    @Override
    public int compareTo(Script o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Script script = (Script) o;
        return Objects.equals(name, script.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Script{" +
                "name='" + name + '\'' +
                ", executable='" + executable + '\'' +
                ", commandLine='" + commandLine + '\'' +
                '}';
    }
}
