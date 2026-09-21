package com.teto;

import com.teto.command.Context;
import com.teto.command.framework.InitialiseFramework;
import com.teto.command.targets.raven.Raven;
import com.teto.domain.meta.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        String target = property(ctx, Tag.Target);
        if (target != null) {
            switch(target) {
                case "Raven" : ctx.apply(new Raven()); break;
            }
        }
    }

}
