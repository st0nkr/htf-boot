package com.teto.domain.cli;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CliArgs {
    private final List<String> args;

    public CliArgs(List<String> args) {
        this.args = args;
    }
}
