package com.teto.command.framework;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.command.properties.LoadProperties;
import com.teto.domain.cli.CliArgs;
import com.teto.service.Database;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@Setter
@Getter
public class InitialiseFramework extends AbstractCommand<Void> {
    private String[] args;

    @Autowired
    private Database database;

    @Override
    public Optional<Void> apply(Context ctx) {
        ctx.stash(new CliArgs(Arrays.asList(args)));
        ctx.stash(database);
        ctx.apply(new LoadProperties());
        ctx.apply(new InitialiseDatabase());
        return Optional.empty();
    }

}
