package com.teto;

import com.teto.command.Context;
import com.teto.domain.file.FileExtension;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.script.ScriptCategory;

public interface IHydra {

    default Script hydraFTP(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.HydraFtp.name());
        s.setProxyChains(true);
        s.setExecutable("hydra");
        String cmd = "-s $port(ftp) -L $userNamesFile -P $passwordsFile -o $json -b json -f -q $ip ftp";
        s.setOutputFormat(FileExtension.json.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.FTP.name());
        return s;
    }

    default Script hydraSSH(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.HydraSSH.name());
        s.setProxyChains(true);
        s.setExecutable("hydra");
        String cmd = "-l $userName -P $wordList $url -o $json -b json";
        s.setOutputFormat(FileExtension.json.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.SSH.name());
        return s;
    }

}
