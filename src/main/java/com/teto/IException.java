package com.teto;

import com.teto.command.Context;
import com.teto.domain.exception.PendingCompletionException;

public interface IException {

    default PendingCompletionException pendingException(Context ctx, String msg) {
        PendingCompletionException pce = new PendingCompletionException(msg);
        return pce;
    }
}
