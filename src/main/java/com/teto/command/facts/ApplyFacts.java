package com.teto.command.facts;

import com.teto.*;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.local.TargetNode;
import com.teto.domain.meta.Tag;
import com.teto.domain.url.Url;
import com.teto.domain.database.Database;
import java.util.*;

public class ApplyFacts extends AbstractCommand<Void> implements ITargetNode, IString, ISet, IPeas,IFact, IUrl {
    private final TargetNode node;

    public ApplyFacts(TargetNode node) {
        this.node = node;
    }

    private Collection<String> wordPressDatabaseCredentials(Context ctx) {
        Set<String> sections = toSet("all");
        Set<String> subSections = toSet("all");
        Set<String> keyWords = toSet("wp-config.php", "DB_NAME");
        return lines(ctx, node,sections, subSections,keyWords);
    }
    private Collection<String> database(Context ctx, String db) {
        Set<String> sections = toSet("all");
        Set<String> subSections = toSet("all");
        Set<String> keyWords = toSet(db);
        return lines(ctx, node,sections, subSections,keyWords);
    }

    private Database databases(Context ctx, TargetNode node) {
        Map<Database, Integer> map = new HashMap<>();
        map.put(Database.Postgresql,database(ctx, "postgres").size());
        map.put(Database.MySQL, database(ctx, "mysql").size());
        map.put(Database.Oracle, database(ctx, "oracle").size());
        map.put(Database.DB2, database(ctx, "db2").size());
        map.put(Database.Informix, database(ctx, "informix").size());
        map.put(Database.SQLServer, database(ctx, "mssql").size());
        map.put(Database.Mongo,database(ctx, "mongo").size());
        map.put(Database.Maria,database(ctx, "mariadb").size());
        Integer maxCount = map.values().stream().max(Integer::compare).get();
        return map.entrySet().stream().filter(e -> e.getValue().equals(maxCount)).map(Map.Entry::getKey).findFirst().get();
    }

    void checkWordPress(Context ctx) {
        Collection<Url> wordpress = getMatchingUrlsContains(ctx, node, "wordpress", "wordpress/");
        if(wordpress != null && !wordpress.isEmpty()) {
            addFact(ctx, node, Tag.WordPress, true, 100);
        }
    }

    private void checkDatabase(Context ctx) {
        Database db = databases(ctx, node);
        if(db != null) {
            addFact(ctx, node, Tag.Database, db.name(), 80);
        }
    }
    @Override
    public Optional<Void> apply(Context ctx) {
        checkWordPress(ctx);
        checkDatabase(ctx);
        if(hasFact(ctx, node, Tag.WordPress)) {
            Collection<String> creds = wordPressDatabaseCredentials(ctx);
            for (String cred : creds) {
                creds.addAll(extractSingleQuotedStrings(cred));
            }

        }
        return Optional.empty();
    }
}
