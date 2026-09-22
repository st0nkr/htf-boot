package com.teto;

import com.teto.command.Context;
import com.teto.domain.file.FileExtension;
import com.teto.domain.meta.Tag;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.script.ScriptCategory;

public interface IKatana {

    default Script katana(Context ctx) {
        Script s = new Script();
        s.setProxyChains(true);
        s.setName(Provenance.Katana.name());
        s.setExecutable("katana");
        String cmd = "-u $url -td -no-color -o $txt";
        s.setOutputFormat(FileExtension.txt.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.DIRECTORIES.name());
        return s;
    }
}
