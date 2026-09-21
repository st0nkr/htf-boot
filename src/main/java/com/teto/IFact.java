package com.teto;

import com.teto.command.Context;
import com.teto.domain.fact.Fact;
import com.teto.domain.local.TargetNode;
import com.teto.domain.meta.Tag;

import java.util.List;

public interface IFact {

    default void addFact(Context ctx, TargetNode nod, Tag name, Boolean value, Integer confidence) {
        Fact fact = new Fact(name, Boolean.toString(value), confidence);
        addFact(ctx, nod, fact);
    }
    default void addFact(Context ctx, TargetNode nod, Tag name, String value, Integer confidence) {
        Fact fact = new Fact(name, value, confidence);
        addFact(ctx, nod, fact);
    }
    default void addFact(Context ctx, TargetNode node, Fact fact) {
        node.getFacts().add(fact);
    }
    default Fact getFact(Context ctx, TargetNode node, Tag name) {
        List<Fact> facts = node.getFacts();
        return facts.stream().filter(fact -> fact.getName().equals(name)).findFirst().orElse(null);
    }
    default boolean hasFact(Context ctx, TargetNode node, Tag name) {
        return getFact(ctx, node, name) != null;
    }

    default boolean hasFact(Context ctx, TargetNode node, Tag name, String value) {
        Fact fact = getFact(ctx, node, name);
        if(fact == null) {
            return false;
        }
        return fact.getValue().equals(value);
    }

    default boolean hasFactConfidence(Context ctx, TargetNode node, Tag name, Integer confidence) {
        if(!hasFact(ctx, node, name)) {
            return false;
        }
        Fact fact = getFact(ctx, node, name);
        return fact.getConfidence() >= confidence;
    }
}
