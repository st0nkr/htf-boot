package com.teto;

import com.teto.command.Context;
import com.teto.domain.version.VersionRange;

import java.util.List;

public interface IVersion {
    default List<String> generateVersions(Context ctx, String startVersion, String endVersion) {
        return VersionRange.generate(startVersion, endVersion);
    }

    default String toVersionNumber(String version) {
        if(!version.contains(".")) {
            return version+".0";
        }
        return version;
    }
}
