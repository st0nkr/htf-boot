package com.teto;

import com.teto.command.Context;
import com.teto.domain.cli.CliArgs;
import com.teto.domain.script.Script;
import com.teto.domain.target.Target;

public interface ICli {
    default CliArgs getCliArgs(Context ctx) {
        return ctx.fetch(CliArgs.class);
    }

    default String createCommand(Context ctx, Script script, IScriptArgProvider sap)  {
        String cmd = script.getCommandLine();
        if(cmd.contains("$userName")) {
            cmd = cmd.replace("$userName", sap.getUserName());
        }
        if(cmd.contains("$spoofMac")) {
            cmd = cmd.replace("$spoofMac", sap.getSpoofMAC());
        }
        if(cmd.contains("$subnetMask")) {
            cmd = cmd.replace("$subnetMask", sap.getSubnetMask());
        }
        if(cmd.contains("$xml")) {
            cmd = cmd.replace("$xml", sap.getOutputFileName());
        }
        if(cmd.contains("$txt")) {
            cmd = cmd.replace("$txt", sap.getOutputFileName());
        }
        if(cmd.contains("$json")) {
            cmd = cmd.replace("$json", sap.getOutputFileName());
        }

        if(cmd.contains("$url")) {
            cmd = cmd.replace("$url", sap.getUrl());
        }

        if(cmd.contains("$userAgent")) {
            cmd = cmd.replace("$userAgent", sap.getUserAgent());
        }

        if(cmd.contains("$wordList")) {
            cmd = cmd.replace("$wordList", sap.getWordList());
        }
        return cmd;
    }
}
