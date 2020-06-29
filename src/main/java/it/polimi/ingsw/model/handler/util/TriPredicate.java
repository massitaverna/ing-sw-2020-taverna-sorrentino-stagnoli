package it.polimi.ingsw.model.handler.util;

import java.util.Objects;

@FunctionalInterface
public interface TriPredicate<T, U, R> {
    boolean test(T t, U u, R r);

    default TriPredicate<T, U, R> negate() {
        return (t, u, r) -> !test(t, u, r);
    }

    default TriPredicate<T, U, R> and(TriPredicate<? super T, ? super U, ? super R> other) {
        Objects.requireNonNull(other);
        return (t, u, r) -> test(t, u, r) && other.test(t, u, r);
    }

    default TriPredicate<T, U, R> or(TriPredicate<? super T, ? super U, ? super R> other) {
        Objects.requireNonNull(other);
        return (t, u, r) -> test(t, u, r) || other.test(t, u, r);
    }
}
