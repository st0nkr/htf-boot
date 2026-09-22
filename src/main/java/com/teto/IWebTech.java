package com.teto;

import com.teto.command.Context;
import com.teto.domain.file.FileExtension;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.script.ScriptCategory;

public interface IWebTech  {
    default Script webTech(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.WebTech.name());
        s.setExecutable("webtech");
        String cmd = "-u $url --user-agent $userAgent --json";
        s.setOutputFormat(FileExtension.json.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.TECHNOLOGY.name());
        return s;
    }
}
