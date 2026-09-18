package com.teto.domain.passwd;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PasswdItem {
    private Long parentId;
    private String provenance;
    private Integer level;

    private String userName;
    private String password;
    private Integer uid;
    private Integer gid;
    private String userInfo;
    private String homeDirectory;
    private String shell;

    public PasswdItem(String un, String pw, Integer uid, Integer gid, String info, String home, String shell) {
        this.userName = un;
        this.password = pw;
        this.uid = uid;
        this.gid = gid;
        this.userInfo = info;
        this.homeDirectory = home;
        this.shell = shell;
    }

}
