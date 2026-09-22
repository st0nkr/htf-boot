package com.teto.domain.local;

import com.teto.domain.fact.Fact;
import com.teto.domain.parser.linpeas.LinPeasResult;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.stage.Stage;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;
import com.teto.domain.target.TargetType;
import com.teto.domain.user.ScannedUser;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
public class TargetNode {
    private final Target target;
    private TargetType targetType;
    private Collection<Target> services = new ArrayList<>();
    private ScannedTargets scannedTargets;
    private Map<Provenance, String> privilegeEscalationScripts = new HashMap<>();
    private List<String> directoriesCreated = new ArrayList<>();
    private List<String> passwordFiles = new ArrayList<>();
    private LinPeasResult peas;
    private Collection<ScannedUser> wordpressUsers = new TreeSet<>();
    private List<Fact> facts = new ArrayList<>();
    private String wordList;
    private String checkPoint;
    private List<Stage> stagesCompleted = new ArrayList<>();
    public TargetNode(Target target) {
        this.target = target;
    }

    public boolean addSubStage(Stage stage) {
        if(stagesCompleted.isEmpty()) {
            return false;
        }
        stage = stagesCompleted.get(stagesCompleted.size() - 1);
        stage.addSubStage(stage);
        return true;
    }
}
