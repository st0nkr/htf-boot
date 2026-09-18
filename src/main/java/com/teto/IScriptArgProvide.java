package com.teto;

import com.teto.command.Context;
import com.teto.domain.script.Script;
import com.teto.domain.target.Target;

public interface IScriptArgProvideFactory extends IMAC, IScripts,IUserAgent {
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
}
