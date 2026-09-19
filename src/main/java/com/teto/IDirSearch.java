package com.teto;

import com.teto.command.Context;
import com.teto.domain.file.FileExtension;
import com.teto.domain.meta.Tag;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.script.ScriptCategory;

public interface IDirSearch extends IProperties {
    default Script dirSearch(Context ctx) {
        Script s = new Script();
        s.setProxyChains(true);
        s.setName(Provenance.DirSearch.name());
        s.setExecutable("dirsearch");
        String cmd = "-u $url --no-color -o $json --format=json -w /usr/share/seclists/Discovery/Web-Content/raft-medium-files.txt";
        s.setOutputFormat(FileExtension.txt.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.DIRECTORIES.name());
        return s;
    }
}
