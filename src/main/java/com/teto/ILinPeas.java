package com.teto;

import com.teto.command.Context;
import com.teto.domain.file.FileExtension;
import com.teto.domain.meta.Tag;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.script.ScriptCategory;

public interface ILinPeas extends IProperties{

    default Script linPeas(Context ctx) {
        Script s = new Script();
        String fileName = property(ctx, Tag.LinPeas);
        s.setName(Provenance.LinPeas.name());
        s.setProxyChains(true);
        s.setExecutable(fileName);
        s.setOutputFormat(FileExtension.txt.name());
        s.setCommandLine(s.getExecutable());
        s.setMainCategory(ScriptCategory.PrivilegeEscalation.name());
        return s;
    }
}
