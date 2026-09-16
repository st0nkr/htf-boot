package com.teto;

import java.util.Optional;

public interface IOptional {
    default <X> Optional<X> nullable(X x) {
        return Optional.ofNullable(x);
    }

    default <X> Optional<X> empty() {
        return Optional.empty();
    }
    default Optional<Boolean> optionalFalse() {
        return Optional.of(false);
    }

    default Optional<Boolean> optionalTrue() {
        return Optional.of(true);
    }

    default <X> Optional<X> optional(X x) {
        if(x != null) {
            return Optional.of(x);
        }
        return nullable(x);
    }

    default boolean isPresent(Optional<?> thing) {
        return thing != null && thing.isPresent();
    }
    default boolean isEmpty(Optional<?> thing) {
        return thing == null || thing.isEmpty();
    }

    default <X> X getObjectOrNull(Optional<X> thing) {
        if(thing != null && thing.isPresent()) {
            return thing.get();
        }
        return null;
    }

    default <X> X getObjectOrValue(Optional<X> thing, X value) {
        if(thing != null && thing.isPresent()) {
            return thing.get();
        }
        return value;
    }
}
