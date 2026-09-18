package com.teto;

import com.teto.command.Context;
import com.teto.domain.file.FileExtension;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;

public interface IWPScan {
    default Script wordPressEnumerateUsers(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.WordPressEnumerateUsers.name());
        s.setExecutable("wpscan");
        String cmd = "--random-user-agent --url $url -o $json -f json --wp-content-dir -at -eu";
        s.setOutputFormat(FileExtension.json.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        return s;
    }
}
