package com.teto.domain.local;

import com.teto.domain.target.Target;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class LocalNetwork implements Serializable {
    private final Target me;
    private List<LocalTarget> localTargets = new ArrayList<>();

    public LocalNetwork(Target me) {
        this.me = me;
    }


}
