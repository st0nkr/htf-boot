package com.teto;

import com.teto.command.Context;
import com.teto.domain.file.FileExtension;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.script.ScriptCategory;

public interface IWhatWeb {
    default Script whatWeb(Context ctx) {
        Script s = new Script();
        s.setName(Provenance.WhatWeb.name());
        s.setExecutable("whatweb");
        String cmd = "$url --user-agent $userAgent --color=never --log-xml=$xml";
        s.setOutputFormat(FileExtension.xml.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.TECHNOLOGY.name());
        return s;
    }
}
