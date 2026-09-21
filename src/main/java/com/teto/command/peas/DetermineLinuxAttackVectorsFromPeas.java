package com.teto.command.peas;

import com.teto.*;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.attack.AttackVector;
import com.teto.domain.local.TargetNode;
import com.teto.domain.meta.Tag;
import com.teto.domain.parser.linpeas.LinPeasParser;
import com.teto.domain.parser.linpeas.LinPeasResult;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.regex.CompositeRegex;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetermineLinuxAttackVectorsFromPeas extends AbstractCommand<Void> implements IString, IFact,ITargetBuilder, IRegex,ISet, IPeas {
    private final TargetNode node;

    public DetermineLinuxAttackVectorsFromPeas(TargetNode node) {
        this.node = node;
    }

    private Collection<String> wordPressCredentials(Context ctx) {
        Set<String> sections = toSet("all");
        Set<String> subSections = toSet("all");
        Set<String> keyWords = toSet("wp-config.php");
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

    private String databases(Context ctx, TargetNode node) {
        Map<String, Integer> map = new HashMap<>();
        map.put("postgres",database(ctx, "postgres").size());
        map.put("mysql", database(ctx, "mysql").size());
        map.put("oracle", database(ctx, "oracle").size());
        map.put("dbs", database(ctx, "db2").size());
        map.put("informix", database(ctx, "informix").size());
        map.put("mssql", database(ctx, "mssql").size());
        map.put("mongo",database(ctx, "mongo").size());
        Integer maxCount = map.values().stream().max(Integer::compare).get();
        return map.entrySet().stream().filter(e -> e.getValue().equals(maxCount)).map(Map.Entry::getKey).findFirst().get();
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
        String database = databases(ctx, node);
        addFact(ctx, node, Tag.Database, database, 80);
        if("mysql".equals(database)) {
            if(hasFact(ctx, node, Tag.WordPress)) {
                final List<String> userNamesPasswords = new ArrayList<>();
                Collection<String> creds = wordPressCredentials(ctx);
                for(String cred : creds) {

                    creds.addAll(extractSingleQuotedStrings(cred));
                }
                System.out.println("Inspect "+creds);
            }
        }
        System.out.println(database);
        final List<AttackVector> vectors = new ArrayList<>();

        return Optional.empty();
    }

    private List<String> extractPatterns(String credential, CompositeRegex cr) {
        final List<String> ret = new ArrayList<>();
        String reg = cr.getRegexPattern();
        String shortened = shortenRegex(cr.getRegexPattern()).getShortenedPattern();
        Pattern regex = Pattern.compile(reg);
        Matcher matcher = regex.matcher(credential);
        while(matcher.find()) {
            String group = matcher.group();
            ret.add(group);
        }
        return ret;
    }


}
