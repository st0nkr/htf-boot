package com.teto.domain.directory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.BaseTarget;
import com.teto.domain.target.TargetType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class Directory extends BaseTarget implements Comparable<Directory> {
    @Meta(tag = Tag.Url, notnull = true)
    private String uri;
    @Meta(tag = Tag.ContentType)
    private String contentType;
    @Meta(tag = Tag.StatusCode)
    private Integer statusCode;
    @Meta(tag = Tag.Hosts)
    private String host;

    public Directory(String name, Long pid, int level) {
        super(name, TargetType.Directory.name(), pid, level);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Directory directory = (Directory) o;
        return Objects.equals(uri, directory.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uri);
    }

    @Override
    public int compareTo(@NonNull Directory o) {
        return getUri().compareTo(o.getUri());
    }
}
