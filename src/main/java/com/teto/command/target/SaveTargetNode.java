package com.teto.command.target;

import com.teto.IFile;
import com.teto.IJSON;
import com.teto.IScripts;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.local.TargetNode;

import java.io.File;
import java.util.Optional;

public class SaveTargetNode extends AbstractCommand<Void> implements IJSON, IFile, IScripts {
    private final TargetNode node;
    private final String checkPoint;

    public SaveTargetNode(TargetNode node, String checkpoint) {
        this.node = node;
        this.checkPoint = checkpoint;
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        String dir = getScanDirectory(ctx);
        node.setCheckPoint(checkPoint);
        String fileName = dir+ File.separator+node.getTarget().getIpAddress()+".json";
        Optional<String> json = toJson(node);
        json.ifPresent(s -> saveFile(fileName, s));
        return Optional.empty();
    }
}
