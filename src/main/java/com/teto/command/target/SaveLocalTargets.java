package com.teto.command.target;

import com.teto.IDatabase;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.local.TargetNetwork;
import com.teto.domain.target.Target;
import com.teto.service.TargetService;

import java.util.Optional;

public class SaveLocalTargets extends AbstractCommand<Void> implements IDatabase {
    private final TargetNetwork net;

    public SaveLocalTargets(TargetNetwork net) {
        this.net = net;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        final TargetService ts = getTargetService(ctx);
        net.getTargetNodes().forEach(ltt-> {
            Target target = ltt.getTarget();
            int count = ts.countByIpAddress(target.getIpAddress());
            if(count == 0) {
              ts.save(target);
            }
        });
        return Optional.empty();
    }
}
