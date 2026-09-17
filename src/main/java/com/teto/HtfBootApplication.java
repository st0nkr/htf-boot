package com.teto;

import com.teto.command.Context;
import com.teto.command.framework.InitialiseFramework;
import com.teto.command.local.FindIPsOnLocalNetwork;
import com.teto.command.properties.LoadProperties;
import com.teto.command.targets.raven.Raven;
import com.teto.domain.cli.CliArgs;
import com.teto.domain.local.LocalNetwork;
import com.teto.domain.meta.Tag;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.Target;
import com.teto.service.Database;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class HtfBootApplication implements CommandLineRunner, ILocalNetwork,IOptional, IProperties {

    public static void main(String[] args) {
        SpringApplication.run(HtfBootApplication.class, args);
    }

    @Autowired
    private InitialiseFramework iframe;
    @Override
    public void run(String... args) throws Exception {
        Context ctx = Teto.getContext();

        iframe.setArgs(args);
        ctx.apply(iframe);
        Optional<LocalNetwork> ln = getLocalNetwork(ctx, true);
        String target = property(ctx, Tag.Target);
        switch(target) {
            case "Raven" : ctx.apply(new Raven(ln.get())); break;
        }
    }
}
