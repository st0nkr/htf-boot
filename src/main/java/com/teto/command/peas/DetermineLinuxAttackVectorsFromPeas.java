package com.teto.command.peas;

import com.teto.IPeas;
import com.teto.IRegex;
import com.teto.ISet;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.attack.AttackVector;
import com.teto.domain.local.TargetNode;
import com.teto.domain.parser.linpeas.LinPeasParser;
import com.teto.domain.parser.linpeas.LinPeasResult;
import com.teto.domain.provenance.Provenance;

import java.util.*;
import java.util.regex.Pattern;

public class DetermineLinuxAttackVectorsFromPeas extends AbstractCommand<Void> implements IRegex,ISet, IPeas {
    private final TargetNode node;

    public DetermineLinuxAttackVectorsFromPeas(TargetNode node) {
        this.node = node;
    }

    private Collection<String> credentials(Context ctx) {
        Set<String> sections = toSet("all");
        Set<String> subSections = toSet("all");
        Set<String> keyWords = toSet("DB_NAME","DB_USER","DB_PASSWORD");
        return lines(ctx, node,sections, subSections,keyWords);
    }
    private Collection<String> credentials2(Context ctx) {
        Set<String> sections = toSet("all");
        Set<String> subSections = toSet("all");
        Set<Pattern> keyWords = toSet(patterns(userName, password));
        return lineMatcher(ctx, node,sections, subSections,keyWords);
    }

    private Collection<String> database(Context ctx, String db) {
        Set<String> sections = toSet("all");
        Set<String> subSections = toSet("all");
        Set<String> keyWords = toSet(db);
        return lines(ctx, node,sections, subSections,keyWords);
    }

    private String databases(Context ctx) {
        Map<String, Integer> map = new HashMap<>();
        map.put("postgres",database(ctx, "postgres").size());
        map.put("mysql", database(ctx, "mysql").size());
        map.put("oracle", database(ctx, "oracle").size());
        map.put("dbs", database(ctx, "db2").size());
        map.put("informix", database(ctx, "informix").size());
        map.put("mssql", database(ctx, "mssql").size());
        map.put("mongo",database(ctx, "mongo").size());
        Integer maxCount = map.values().stream().max(Integer::compare).get();
        String maxKey = map.entrySet().stream().filter(e -> e.getValue().equals(maxCount)).map(Map.Entry::getKey).findFirst().get();
        return maxKey;
    }

    private Collection<String> maxCollection(Collection<String> postgres, Collection<String> mysql, Collection<String> oracle, Collection<String> db2, Collection<String> informix, Collection<String> mssql, Collection<String> mongo) {
        List<Collection<String>> lists = Arrays.asList(postgres,mysql,oracle,db2,informix,mssql,mongo);
        Collections.sort(lists, (o1, o2) -> {
            if (o1.size() > o2.size()) return -1;
            if (o1.size() < o2.size()) return 1;
            return 0;
        });
        return lists.get(0);
    }

    @Override
    public Optional<Void> apply(Context ctx) {
        if(node.getPeas() == null) {
            info(this, "Parsing linpeas output");
            String linPas = node.getPrivilegeEscalationScripts().get(Provenance.LinPeas);
            LinPeasParser parser = new LinPeasParser();
            LinPeasResult peas = parser.parse(ctx, linPas);
            node.setPeas(peas);
        }
        Collection<String> credentials = credentials(ctx);
        Collection<String> credentials2 = credentials2(ctx);
        String database = databases(ctx);
        System.out.println(credentials);
        System.out.println(database);
        final List<AttackVector> vectors = new ArrayList<>();

        return Optional.empty();
    }


}
