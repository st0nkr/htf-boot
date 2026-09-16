package com.teto;

import com.teto.command.Context;
import com.teto.domain.service.KnownService;
import com.teto.service.KnownServicesService;

import java.util.List;

public interface IKnownServices extends IDatabase, IProperties, ICSV{

    default List<KnownService> getKnownServices(Context ctx, Long portNumber, String protocol) {
        KnownServicesService ks = getDatabase(ctx).getKnownServices();
        return ks.findByPortNumberAndProtocol(portNumber, protocol);
    }

}
