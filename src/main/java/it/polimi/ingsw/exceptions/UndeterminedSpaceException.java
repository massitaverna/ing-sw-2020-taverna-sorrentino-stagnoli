package it.polimi.ingsw.exceptions;

public class UndeterminedSpaceException extends RuntimeException {
    public UndeterminedSpaceException() {};
    public UndeterminedSpaceException(String message) {
        super(message);
    }
}
