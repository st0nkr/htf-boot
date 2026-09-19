package com.teto;

import com.teto.command.Context;
import com.teto.domain.file.FileExtension;
import com.teto.domain.meta.Tag;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.script.ScriptCategory;

public interface ILinEnum extends IProperties{

    default Script linEnum(Context ctx) {
        Script s = new Script();
        String fileName = property(ctx, Tag.LinEnum);
        s.setName(Provenance.LinEnum.name());
        s.setProxyChains(true);
        s.setExecutable(fileName);
        s.setOutputFormat(FileExtension.txt.name());
        s.setCommandLine(s.getExecutable());
        s.setMainCategory(ScriptCategory.PrivilegeEscalation.name());
        return s;
    }
}
