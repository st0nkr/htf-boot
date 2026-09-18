package com.teto;

import com.teto.command.Context;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.Target;

public interface IUserAgent extends IRandom, IFile, IProperties {

    default String userAgentFromTarget(Context ctx, Target t) {
       return randomFirefox(ctx);
    }

    default String randomUserAgentFromFile(Context ctx, String fileName) {
        var userAgents = readFileAsLines(fileName);
        if(userAgents.isEmpty()) {
            return null;
        }
        int rnd = randomInt(0, userAgents.get().size() - 1);
        return userAgents.get().get(rnd);
    }

    default String randomAndroid(Context ctx) {
        String fileName = property(ctx, Tag.AndroidUserAgentsFile);
        return randomUserAgentFromFile(ctx, fileName);
    }

    default String randomFirefox(Context ctx) {
        String fileName = property(ctx, Tag.FirefoxUserAgentsFile);
        return randomUserAgentFromFile(ctx, fileName);
    }
    default String randomTOR(Context ctx) {
        String fileName = property(ctx, Tag.FirefoxUserAgentsFile);
        return randomUserAgentFromFile(ctx, fileName);
    }
    default String randomChrome(Context ctx) {
        String fileName = property(ctx, Tag.ChromeUserAgentsFile);
        return randomUserAgentFromFile(ctx, fileName);
    }
    default String randomEdge(Context ctx) {
        String fileName = property(ctx, Tag.EdgeUserAgentsFile);
        return randomUserAgentFromFile(ctx, fileName);
    }
    default String randomIE(Context ctx) {
        String fileName = property(ctx, Tag.IEUserAgentsFile);
        return randomUserAgentFromFile(ctx, fileName);
    }
    default String randomOpera(Context ctx) {
        String fileName = property(ctx, Tag.OperaUserAgentsFile);
        return randomUserAgentFromFile(ctx, fileName);
    }
    default String randomSafari(Context ctx) {
        String fileName = property(ctx, Tag.SafariUserAgentsFile);
        return randomUserAgentFromFile(ctx, fileName);
    }

    default String getUserAgent(Context ctx) {
        String ua = property(ctx, Tag.UserAgent);
        if("random".equalsIgnoreCase(ua)) {
            String bn = property(ctx, Tag.BrowserName);
            if(bn == null) {
                bn = "chrome";
            }
            return randomUserAgentFromFile(ctx, bn);
        }
        return randomUserAgentFromFile(ctx, ua);
    }
}
