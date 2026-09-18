package com.teto;

import com.teto.command.Context;
import com.teto.domain.target.Target;

public interface IConverter extends IUserAgent {

    default String toUrl(Target t) {
        Long portNo = t.getPortNumber();
        if(portNo == null) {
            return "http://"+t.getIpAddress();
        }
        if(portNo == 80) {
            return "http://"+t.getIpAddress();
        }
        if(portNo == 443) {
            return "https://"+t.getIpAddress();
        }
        return t.getUri();
    }

    default String toUserAgent(Context ctx, Target t) {
        return userAgentFromTarget(ctx, t);
    }
}
