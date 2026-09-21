package com.teto;

import com.teto.command.Context;
import com.teto.domain.file.FileExtension;
import com.teto.domain.meta.Tag;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;

public interface IDirBuster extends IProperties {

    default Script dirBuster(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.DirBuster.name());
        s.setExecutable("dirbuster");
        String cmd = "-u $url -l $wordList -H -r $txt";
        s.setOutputFormat(FileExtension.json.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        return s;
    }
}
