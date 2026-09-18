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
public class TargetNetwork implements Serializable {
    private final Target me;
    private List<TargetNode> targetNodes = new ArrayList<>();

    public TargetNetwork(Target me) {
        this.me = me;
    }


}
