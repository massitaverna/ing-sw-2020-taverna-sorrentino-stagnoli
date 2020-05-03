package it.polimi.ingsw.model.handler.util;

public class Pair<T> {
    private final T first;
    private final T second;

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public T get(int x) {
        if (x == 0) {
            return first;
        }
        else if (x==1) {
            return second;
        }

        throw new IllegalArgumentException("A pair does not have the " + x + "-th element.");
    }

}
