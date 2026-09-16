package com.teto.domain.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class ApiEndPoint implements Comparable<ApiEndPoint>{
    @Meta(tag = Tag.ID, id = true)
    private Integer id;
    @Meta(tag = Tag.ParentId, notnull = true)
    private Integer parentId;
    @Meta(tag = Tag.Level)
    private Integer level;
    @Meta(tag = Tag.ParentType)
    private String parentType;
    @Meta(tag = Tag.Provenance, notnull = true)
    private String provenance;
    @Meta(tag = Tag.Method)
    private String method;
    @Meta(tag = Tag.Target)
    private String target;
    @Meta(tag = Tag.Path)
    private String path;
    @Meta(tag = Tag.StatusCode)
    private Integer statucCode;
    @Meta(tag = Tag.Length)
    private Integer length;
    @Meta(tag = Tag.Title)
    private String title;
    @Meta(tag = Tag.Description)
    private String description;
    @Meta(tag = Tag.Severity)
    private String severity;
    @Meta(tag = Tag.Remediation)
    private String remediation;
    @Meta(tag = Tag.Evidence)
    private String evidence;
    @Meta(tag = Tag.Module)
    private String module;

    @Override
    public int compareTo(ApiEndPoint o) {
        int cmp;
        if(getTarget() != null && o.getTarget() != null) {
            cmp = getTarget().compareTo(o.getTarget());
            if(cmp != 0) {
                return cmp;
            }
        }
        if(getPath() != null && o.getPath() != null) {
            cmp = getPath().compareTo(o.getPath());
            if(cmp != 0) {
                return cmp;
            }
        }
        if(getMethod() != null && o.getMethod() != null) {
            return getMethod().compareTo(o.getMethod());
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ApiEndPoint that = (ApiEndPoint) o;
        return Objects.equals(method, that.method) && Objects.equals(target, that.target) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, target, path);
    }
}
