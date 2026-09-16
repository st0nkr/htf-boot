package com.teto;

import com.teto.command.Context;

public enum Teto {
    INSTANCE(new Context());
    private Context ctx;

    Teto(Context ctx) {
        this.ctx = ctx;
    }

    public static Context getContext() {
        return INSTANCE.ctx;
    }

    public static Context cloneContext() {
        Context clone = getContext().clone();
        return clone;
    }
}
