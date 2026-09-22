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
    @Override
    public Optional<Void> apply(Context ctx) {
        if(node.getPeas() == null) {
            info(this, "Parsing linpeas output");
            String linPas = node.getPrivilegeEscalationScripts().get(Provenance.LinPeas);
            LinPeasParser parser = new LinPeasParser();
            LinPeasResult peas = parser.parse(ctx, linPas);
            node.setPeas(peas);
        }
        final List<AttackVector> vectors = new ArrayList<>();

        return Optional.empty();
    }
}
