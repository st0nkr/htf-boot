package com.teto.domain.user;

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
public class ScannedUser implements Comparable<ScannedUser>{
    @Meta(tag = Tag.ID, id = true)
    private Integer id;
    @Meta(tag = Tag.ParentId)
    private Integer parentId;
    @Meta(tag = Tag.Provenance)
    private String provenance;
    @Meta(tag = Tag.FirstName)
    private String firstName;
    @Meta(tag = Tag.MiddelNames)
    private String middleNames;
    @Meta(tag = Tag.Surname)
    private String surname;
    @Meta(tag = Tag.Email)
    private String email;
    @Meta(tag = Tag.UserName)
    private String userName;
    @Meta(tag = Tag.Password)
    private String password;
    @Meta(tag = Tag.ParentType)
    private String parentType;
    @Meta(tag = Tag.Level)
    private Integer level;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ScannedUser user = (ScannedUser) o;
        return Objects.equals(firstName, user.firstName) && Objects.equals(middleNames, user.middleNames) && Objects.equals(surname, user.surname) && Objects.equals(email, user.email) && Objects.equals(userName, user.userName) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, middleNames, surname, email, userName, password);
    }

    @Override
    public int compareTo(ScannedUser o) {
        int cmp = getUserName().compareTo(o.getUserName());
        if(cmp != 0) return cmp;
        cmp = getEmail().compareTo(o.getEmail());
        if(cmp != 0) return cmp;
        return cmp;
    }
}
