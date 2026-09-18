package com.teto;

import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.script.Script;
import com.teto.domain.target.Target;
import com.teto.domain.user.ScannedUser;

public interface IScriptArgProvide extends IMAC, IScripts,IUserAgent, IPort {

    default IScriptArgProvider sap(Context ctx, Target t, final Script script, final String url) {
        return new IScriptArgProvider() {

            @Override
            public String getSpoofMAC() {
                return generateRandomMacAddress();
            }

            @Override
            public String getSubnetMask() {
                return t.getSubNetMask();
            }

            @Override
            public String getOutputFileName() {
                return createFileName(ctx, t, script);
            }

            @Override
            public String getUrl() {
                return url;
            }

            @Override
            public String getUserAgent() {
                return randomFirefox(ctx);
            }

            @Override
            public String getWordList() {
                return "";
            }

            @Override
            public String getUserName() {
                return "";
            }
        };
    }

    default IScriptArgProvider sap(final Context ctx, final TargetNode t, final Script script, final ScannedUser user, final String wordList) {
        return new IScriptArgProvider() {
            @Override
            public String getSpoofMAC() {
                return generateRandomMacAddress();
            }

            @Override
            public String getSubnetMask() {
                return t.getTarget().getSubNetMask();
            }

            @Override
            public String getOutputFileName() {
                final Target target = t.getTarget();
                return createFileName(ctx, target, script).replace(script.getName(),user.getUserName()+"-"+script.getName());
            }

            @Override
            public String getUrl() {
                final Target target = t.getTarget();
                return "ssh://" + target.getIpAddress() + ":"+getPort(ctx, t, "ssh");
            }

            @Override
            public String getUserAgent() {
                return randomFirefox(ctx);
            }

            @Override
            public String getWordList() {
                return wordList;
            }

            @Override
            public String getUserName() {
                return user.getUserName();
            }
        };
    }
}
