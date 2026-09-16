package com.teto.command.product;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.product.Product;
import com.teto.domain.target.TargetType;

import java.util.Optional;

public class IdentifyProductTargetType extends AbstractCommand<TargetType> {
    private final Product product;
    private final String source;

    public IdentifyProductTargetType(Product product, String source) {
        this.product = product;
        this.source = source;
    }

    @Override
    public Optional<TargetType> apply(Context ctx) {
        if(source != null && source.toLowerCase().contains("firewall")) {
            return optional(TargetType.Firewall);
        }

        TargetType tt = TargetType.fromString(product.getName());
        if(tt != null) {
            return optional(tt);
        }
        return Optional.empty();
    }
}
