package com.teto;

import com.teto.command.Context;
import com.teto.command.exec.RunCommand;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.file.FileExtension;
import com.teto.domain.local.TargetNode;
import com.teto.domain.meta.Tag;
import com.teto.domain.parser.ParserRequest;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.script.ScriptManager;
import com.teto.domain.target.Target;

import java.io.File;
import java.util.Optional;

public interface IScripts extends IOptional, IDuration, ILogger, IProperties, IFile, IConverter, IAttackVector, IMAC {
    default ScriptManager getScriptManager(Context ctx) {
        ScriptManager sm = ctx.fetch(ScriptManager.class);
        if(sm == null) {
            sm = new ScriptManager();
            ctx.stash(ScriptManager.class, sm);
        }
        return sm;
    }

    default Optional<Script> getScript(Context ctx, Provenance prov) {
        ScriptManager sm = getScriptManager(ctx);
        if(sm != null) {
            return optional(sm.getScript(ctx, prov));
        }
        return empty();
    }

    default Optional<RunCommandResponse> runScript(Context ctx, Target target, Script script, IScriptArgProvider sap) {
        ParserRequest pr = new ParserRequest(target, Provenance.fromString(script.getName()));
        pr.setOutputFileName(sap.getOutputFileName());
        info(this,"Output -> "+pr.getOutputFileName());
        boolean forceReScan = propertyBoolean(ctx, Tag.ForceReScan, false);
        // Create a CliMapper for script
        if(forceReScan || !fileExists(pr.getOutputFileName())) {
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
            info(this,"Command -> "+cmd);
            Optional<RunCommandResponse> rsp = ctx.apply(new RunCommand(cmd, 0, minutes(30)));
            rsp.ifPresent(runCommandResponse -> runCommandResponse.setOutputFileName(pr.getOutputFileName()));
            return rsp;
        }
        return empty();
    }

    default Optional<RunCommandResponse> runScript(Context ctx, TargetNode node, Script script) {
        Target target = node.getTarget();
        ParserRequest pr = new ParserRequest(target, Provenance.fromString(script.getName()));
        pr.setOutputFileName(createFileName(ctx, target, script));
        info(this,"Output -> "+pr.getOutputFileName());
        boolean forceReScan = propertyBoolean(ctx, Tag.ForceReScan, false);
        // Create a CliMapper for script
        if(forceReScan || !fileExists(pr.getOutputFileName())) {
            String cmd = script.getCommandLine();
            if(cmd.contains("$spoofMac")) {
                cmd = cmd.replace("$spoofMac", generateRandomMacAddress());
            }
            if(cmd.contains("$subnetMask")) {
                cmd = cmd.replace("$subnetMask", target.getSubNetMask());
            }
            if(cmd.contains("$xml")) {
                cmd = cmd.replace("$xml", pr.getOutputFileName());
            }
            if(cmd.contains("$txt")) {
                cmd = cmd.replace("$txt", pr.getOutputFileName());
            }
            if(cmd.contains("$json")) {
                cmd = cmd.replace("$json", pr.getOutputFileName());
            }

            if(cmd.contains("$url")) {
                cmd = cmd.replace("$url", getWebServerUrl(ctx, node));
            }

            if(cmd.contains("$userAgent")) {
                cmd = cmd.replace("$userAgent", toUserAgent(ctx, target));
            }
            info(this,"Command -> "+cmd);
            Optional<RunCommandResponse> rsp = ctx.apply(new RunCommand(cmd, 0, minutes(30)));
            rsp.ifPresent(runCommandResponse -> runCommandResponse.setOutputFileName(pr.getOutputFileName()));
            return rsp;
        }
        return empty();
    }
    default Optional<RunCommandResponse> runScript(Context ctx, Target target, Script script) {
        ParserRequest pr = new ParserRequest(target, Provenance.fromString(script.getName()));
        pr.setOutputFileName(createFileName(ctx, target, script));
        info(this,"Output -> "+pr.getOutputFileName());
        boolean forceReScan = propertyBoolean(ctx, Tag.ForceReScan, false);
        // Create a CliMapper for script
        if(forceReScan || !fileExists(pr.getOutputFileName())) {
            String cmd = script.getCommandLine();
            if(cmd.contains("$spoofMac")) {
                cmd = cmd.replace("$spoofMac", generateRandomMacAddress());
            }
            if(cmd.contains("$subnetMask")) {
                cmd = cmd.replace("$subnetMask", target.getSubNetMask());
            }
            if(cmd.contains("$xml")) {
                cmd = cmd.replace("$xml", pr.getOutputFileName());
            }
            if(cmd.contains("$txt")) {
                cmd = cmd.replace("$txt", pr.getOutputFileName());
            }
            if(cmd.contains("$json")) {
                cmd = cmd.replace("$json", pr.getOutputFileName());
            }

            if(cmd.contains("$url")) {
                cmd = cmd.replace("$url", toUrl(target));
            }

            if(cmd.contains("$userAgent")) {
                cmd = cmd.replace("$userAgent", toUserAgent(ctx, target));
            }
            info(this,"Command -> "+cmd);
            Optional<RunCommandResponse> rsp = ctx.apply(new RunCommand(cmd, 0, minutes(30)));
            rsp.ifPresent(runCommandResponse -> runCommandResponse.setOutputFileName(pr.getOutputFileName()));
            return rsp;
        }
        return empty();
    }

    default String createFileName(Context ctx, Target target, Script script) {
        String dir = property(ctx, Tag.ScanDirectory);
        String xtn = getExtension(script);
        return dir+ File.separator+script.getName()+"-"+target.getIpAddress()+xtn;
    }

    default String getExtension(Script script) {
        FileExtension xtn = FileExtension.fromString(script.getOutputFormat());
        if(xtn == null) {
            return ".txt";
        }
        return "."+xtn.name();
    }
}
