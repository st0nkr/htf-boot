package com.teto;

import com.teto.command.Context;
import com.teto.service.CustomerService;
import com.teto.service.Database;
import com.teto.service.KnownServicesService;
import com.teto.service.TargetService;

public interface IDatabase {
    default Database getDatabase(Context ctx) {
        return ctx.fetch(Database.class);
    }

    default TargetService getTargetService(Context ctx) {
        return getDatabase(ctx).getTargets();
    }

    default CustomerService getCustomerService(Context ctx) {
        return getDatabase(ctx).getCustomers();
    }

    default KnownServicesService getKnownServices(Context ctx) {
        return getDatabase(ctx).getKnownServices();
    }
}
