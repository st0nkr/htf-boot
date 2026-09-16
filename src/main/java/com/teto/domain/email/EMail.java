package com.teto.domain.email;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class EMail implements Comparable<EMail>{
    @Meta(tag = Tag.ID, id = true)
    private Integer id;
    @Meta(tag = Tag.ParentId, notnull = true)
    private Integer parentId;
    @Meta(tag = Tag.Provenance)
    private String provenance;
    @Meta(tag = Tag.Address)
    private String address;
    @Meta(tag = Tag.FirstName)
    private String firstName;
    @Meta(tag = Tag.Surname)
    private String surname;
    @Meta(tag = Tag.UserName)
    private String userName;
    @Meta(tag = Tag.Password)
    private String password;
    @Meta(tag = Tag.ParentType)
    private String parentType;
    @Meta(tag = Tag.Level)
    private Integer level;

    public EMail(String addr) {
        this.address = addr;
    }

    @Override
    public int compareTo(@NonNull EMail o) {
        return getAddress().compareTo(o.getAddress());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EMail eMail = (EMail) o;
        return Objects.equals(address, eMail.address);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(address);
    }
}
