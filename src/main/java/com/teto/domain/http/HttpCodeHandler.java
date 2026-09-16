package com.teto.domain.http;

import com.teto.command.Context;

public interface HttpCodeHandler {
    void handleStatusCode(HttpResponseCode code, Context ctx);
}
