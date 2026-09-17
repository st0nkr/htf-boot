package com.teto;

import com.teto.command.Context;
import com.teto.command.exec.RunCommand;
import com.teto.command.exec.RunCommandResponse;
import com.teto.domain.file.FileExtension;
import com.teto.domain.meta.Tag;
import com.teto.domain.parser.ParserRequest;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.script.ScriptManager;
import com.teto.domain.target.Target;

import java.io.File;
import java.util.Optional;

public interface IScripts extends IOptional, IDuration, ILogger, IProperties, IFile, IMAC {
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


    default Optional<RunCommandResponse> runScript(Context ctx, Target target, Script script) {
        ParserRequest pr = new ParserRequest(target, Provenance.fromString(script.getName()));
        pr.setOutputFileName(createFileName(ctx, target, script));
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
            info(this,"Command -> "+cmd);
            Optional<RunCommandResponse> rsp = ctx.apply(new RunCommand(cmd, 0, minutes(30)));
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
