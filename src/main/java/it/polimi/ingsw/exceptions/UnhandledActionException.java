package it.polimi.ingsw.exceptions;

public class UnhandledActionException extends Exception {
    public UnhandledActionException() {};
    public UnhandledActionException(String message) {
        super(message);
    }
}
