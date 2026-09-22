package com.teto.domain.stage;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Stage {
    private String name;
    private String status;
    private Long date;
    private List<Stage> subStages;

    public Stage(String stage, String status) {
        this.name = stage;
        this.status = status;
        date = System.currentTimeMillis();
    }

    public Stage(String stage, String status, Long date) {
        this.name = stage;
        this.status = status;
        date = date;
    }

    public void addSubStage(Stage stage) {
        if(subStages == null) {
            subStages = new ArrayList<>();
        }
        subStages.add(stage);
    }
}
