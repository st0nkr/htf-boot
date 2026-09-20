package com.teto;

import com.teto.command.Context;
import com.teto.domain.file.FileExtension;
import com.teto.domain.meta.Tag;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;

public interface INikto extends IProperties {

    default Script nikto(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.Nikto.name());
        String repos = property(ctx, Tag.NiktoRepository);
        s.setRepository(repos);
        String buildCommand = property(ctx, Tag.NiktoBuildCommand);
        s.setBuildCommand(buildCommand);
        s.setExecutable("nikto");
        String cmd = "-host $url -followredirects -output $json";
        s.setOutputFormat(FileExtension.json.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        return s;
    }
}
