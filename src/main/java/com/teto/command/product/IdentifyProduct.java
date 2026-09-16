package com.teto.command.product;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.product.Product;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.target.TargetType;

import java.util.Optional;

public class IdentifyProduct extends AbstractCommand<Product>  {
    private final String source;
    private final Long portId;
    private final Provenance provenance;

    public IdentifyProduct(String source, Long portId, Provenance provenance) {
        this.source = source;
        this.portId = portId;
        this.provenance = provenance;
    }

    @Override
    public Optional<Product> apply(Context ctx) {
        if(source.contains("(RSA)")) {
            return empty();
        }
        Product product = new Product();
        product.setProvenance(provenance);
        product.setPortId(portId);

        product.setVersion(extractProductVersion(ctx, source));
        product.setName(extractProductName(ctx,source));
        if(product.getName() == null) {
            product.setName(source);
        }

        Optional<TargetType> tt = ctx.apply(new IdentifyProductTargetType(product, source));
        tt.ifPresent(targetType -> product.setName(targetType.name()));
        tt.ifPresent(product::setTargetType);
        String os = extractOs(source);
        product.setOs(os);
        return optional(product);
    }

    private String extractOs(String str) {
        int start = str.indexOf(("("));
        int end = str.lastIndexOf((")"));
        if(start == -1 || end == -1) {
            return null;
        }
        String os = str.substring(start+1, end);
        return os;
    }

    private String extractProductVersion(Context ctx, String src) {
        String[] parts = src.split(" ");
        if(parts.length < 2) {
            return null;
        }
        if(parts.length == 2) {
            String[] words = parts[0].split("/");
            if(words.length == 2) {
                return words[1];
            }
        }
        return null;
    }

    private String extractProductName(Context ctx, String src) {
        String[] parts = src.split(" ");
        if(parts.length < 2) {
            return null;
        }
        if(parts.length == 2) {
            String[] words = parts[0].split("/");
            if(words.length == 2) {
                return words[0];
            }
        }
        return null;
    }
}
