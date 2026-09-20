package com.teto.domain.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
@Entity
public class ScannedUser implements Comparable<ScannedUser>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long parentId;
    private String provenance;
    private String firstName;
    private String middleNames;
    private String surname;
    private String email;
    private String userName;
    private String password;
    private String parentType;
    private Integer level;
    private String foundBy;
    private Integer confidence;
    private String context;
    private Integer uid;
    private Integer gid;
    private String userInfo;
    private String homeDirectory;
    private String shell;
    private Boolean sshValid;


    @Override
    public int compareTo(ScannedUser o) {
        int cmp = getUserName().compareTo(o.getUserName());
        if(cmp != 0) return cmp;
        if(getEmail() != null && o.getEmail() != null) {
            cmp = getEmail().compareTo(o.getEmail());
            if (cmp != 0) return cmp;
        }
        if(getSurname() != null && o.getSurname() != null) {
            cmp = getSurname().compareTo(o.getSurname());
            if (cmp != 0) return cmp;
        }
        if(getContext() != null && o.getContext() != null) {
            cmp = getContext().compareTo(o.getContext());
            if (cmp != 0) return cmp;
        }
        return cmp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ScannedUser that = (ScannedUser) o;
        return Objects.equals(id, that.id) && Objects.equals(parentId, that.parentId) && Objects.equals(provenance, that.provenance) && Objects.equals(firstName, that.firstName) && Objects.equals(middleNames, that.middleNames) && Objects.equals(surname, that.surname) && Objects.equals(email, that.email) && Objects.equals(userName, that.userName) && Objects.equals(password, that.password) && Objects.equals(parentType, that.parentType) && Objects.equals(level, that.level) && Objects.equals(foundBy, that.foundBy) && Objects.equals(confidence, that.confidence) && Objects.equals(context, that.context) && Objects.equals(uid, that.uid) && Objects.equals(gid, that.gid) && Objects.equals(userInfo, that.userInfo) && Objects.equals(homeDirectory, that.homeDirectory) && Objects.equals(shell, that.shell) && Objects.equals(sshValid, that.sshValid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, provenance, firstName, middleNames, surname, email, userName, password, parentType, level, foundBy, confidence, context, uid, gid, userInfo, homeDirectory, shell, sshValid);
    }

    @Override
    public String toString() {
        return "ScannedUser{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}
