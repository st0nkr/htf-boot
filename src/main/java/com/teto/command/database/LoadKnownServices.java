package com.teto.command.database;

import com.teto.*;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.meta.Tag;
import com.teto.domain.service.KnownService;
import com.teto.service.KnownServicesService;

import java.util.Optional;

public class LoadKnownServices extends AbstractCommand<Void> implements IProperties, ICSV, IFile, IDatabase {
    final int NAME = 0;
    final int PROT = 1;
    final int PORT = 2;
    final int DESC = 3;
    final int FREQ = 4;

    @Override
    public Optional<Void> apply(Context ctx) {
        boolean load = propertyBoolean(ctx, Tag.LoadKnownServices, false);
        if(load) {
            KnownServicesService ks = getKnownServices(ctx);
            if (ks.countAll() == 0) {
                String fileName = property(ctx, Tag.KnownServicesFile);
                if (fileExists(fileName)) {
                    loadKnownServices(ctx, ks, fileName);
                }
            }
        }
        return Optional.empty();
    }

    private void loadKnownServices(Context ctx, final KnownServicesService ks, String fileName) {
        loadCSVFile(fileName, new CSVVisitor() {
            @Override
            public void handle(int lc, String[] line) {
                KnownService srv = new KnownService();
                if(line.length > 5) {
                    int last = line.length - 1;
                    srv.setUseFrequency(Double.parseDouble(line[last]));
                    StringBuilder sb = new StringBuilder();
                    for(int i = DESC; i < last; i++) {
                        if (sb.length() > 0) sb.append(" ");
                        sb.append(line[i]);
                    }
                    srv.setComment(sb.toString().trim());
                } else {
                    srv.setComment(line[DESC]);
                    srv.setUseFrequency(Double.parseDouble(line[FREQ]));
                }

                srv.setName(line[NAME]);
                srv.setProtocol(line[PROT]);
                srv.setPortNumber(Long.parseLong(line[PORT]));
                try {
                    ks.save(srv);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                System.out.println(srv);
            }

            @Override
            public void handle(int lc, Exception e) {
                e.printStackTrace();
            }
        });
    }
}
