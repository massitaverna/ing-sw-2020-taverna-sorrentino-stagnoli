package it.polimi.ingsw.model.handler;

@FunctionalInterface
public interface TriPredicate<T, U, R> {
    public boolean test(T t, U u, R r);
}
