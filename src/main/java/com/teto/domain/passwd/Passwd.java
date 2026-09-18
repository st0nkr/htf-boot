package com.teto.domain.passwd;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Passwd {
    private Long parentId;
    private String provenance;
    private Integer level;

    private List<PasswdItem> users = new ArrayList<>();

    public boolean add(PasswdItem item) {
        return users.add(item);
    }

}
