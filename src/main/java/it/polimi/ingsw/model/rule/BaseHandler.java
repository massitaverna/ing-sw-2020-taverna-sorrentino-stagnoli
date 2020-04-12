package it.polimi.ingsw.model.rule;

import it.polimi.ingsw.exceptions.UnhandledActionException;

public abstract class BaseHandler implements RuleHandler {
    RuleHandler next;

    public BaseHandler() {}
    public BaseHandler(RuleHandler next) {
        this.next = next;
    }

    public void handle() throws UnhandledActionException {
        if (next != null) {
            next.handle();
        } else {
            throw new UnhandledActionException("Cannot determine correctness for the action.");
        }
    }
}
