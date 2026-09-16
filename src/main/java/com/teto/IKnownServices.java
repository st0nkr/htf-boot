package com.teto;

import com.teto.command.Context;
import com.teto.domain.service.KnownService;

import java.util.List;

public interface IKnownServices extends IProperties, ICSV{

    default List<KnownService> getKnownServices(Context ctx, Long portNumber, String protocol) {
        return null;
    }

}
