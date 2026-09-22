package com.teto;

import com.teto.command.Context;
import com.teto.domain.file.FileExtension;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.script.ScriptCategory;

public interface IWappy {
    default Script wappy(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.Wappy.name());
        s.setExecutable("wappy");
        String cmd = "-u $url --deep --explain --format jsonl";
        s.setOutputFormat(FileExtension.json.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.TECHNOLOGY.name());
        return s;
    }
}
