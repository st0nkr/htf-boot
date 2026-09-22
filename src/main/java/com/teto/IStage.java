package com.teto;

import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.stage.Stage;

public interface IStage {
    default void stageStart(Context ctx, TargetNode tn, String stage) {
        tn.getStagesCompleted().add(new Stage(stage,"Started"));
    }
    default boolean  addSubStage(Context ctx, TargetNode tn, String stage, String status) {
        return tn.addSubStage(new Stage(stage, status));
    }
}
