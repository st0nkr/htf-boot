package com.teto;

import com.teto.command.Context;
import com.teto.command.local.FindIPsOnLocalNetwork;
import com.teto.command.properties.LoadProperties;
import com.teto.domain.local.LocalNetwork;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.Target;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class HtfBootApplication implements CommandLineRunner, ILocalNetwork,IOptional {

    public static void main(String[] args) {
        SpringApplication.run(HtfBootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Context ctx = Teto.getContext();
        ctx.apply(new LoadProperties(Arrays.asList(args)));
        Optional<LocalNetwork> ln = getLocalNetwork(ctx, true);
        if(isPresent(ln)) {
            System.out.println(ln.get());
        }
    }
}
