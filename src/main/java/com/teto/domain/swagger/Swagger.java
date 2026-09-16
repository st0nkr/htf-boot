package com.teto.domain.swagger;

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
public class Swagger implements Comparable<Swagger>{
    @Meta(tag = Tag.ID, id = true)
    private Integer id;
    @Meta(tag = Tag.ParentId, notnull = true)
    private Integer parentId;
    @Meta(tag = Tag.Provenance)
    private String provenance;
    @Meta(tag = Tag.ParentType)
    private String parentType;
    @Meta(tag = Tag.Uri)
    private String uri;
    @Meta(tag = Tag.Level)
    private Integer level;

    public Swagger(String url, String provenance) {
        this.uri = url;
        this.provenance = provenance;
    }

    @Override
    public int compareTo(Swagger o) {
        return getUri().compareTo(o.getUri());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Swagger swagger = (Swagger) o;
        return Objects.equals(uri, swagger.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uri);
    }
}
