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
        if(getEmail() != null && o.getEmail() != null) {
            cmp = getEmail().compareTo(o.getEmail());
            if (cmp != 0) return cmp;
        }
        if(getSurname() != null && o.getSurname() != null) {
            cmp = getSurname().compareTo(o.getSurname());
            if (cmp != 0) return cmp;
        }
        return cmp;
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
