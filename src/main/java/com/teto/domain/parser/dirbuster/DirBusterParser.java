package com.teto.domain.parser.dirbuster;

import com.teto.IFile;
import com.teto.command.Context;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.ScannedTargets;
import com.teto.domain.target.Target;

public class DirBusterParser implements IFile {

    public ScannedTargets parse(Context ctx, Target http, String fileName) {
        ScannedTargets st = new ScannedTargets();
        st.getTargets().forEach(target -> {
            target.setProvenance(Provenance.DirBuster.name());
        });
        st.getContactDetails().forEach(contact -> {
            contact.setProvenance(Provenance.DirBuster);
        });
        return st;
    }
}
