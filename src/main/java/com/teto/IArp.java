package com.teto;

import com.teto.command.Context;
import com.teto.domain.file.FileExtension;
import com.teto.domain.meta.Tag;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.script.ScriptCategory;

public interface IArp extends IProperties{

    default Script arpNames(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.ArpNames.name());
        s.setExecutable("arp");
        String cmd = property(ctx, Tag.ArpNames);
        s.setOutputFormat(FileExtension.txt.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.ARP.name());
        return s;
    }
}
