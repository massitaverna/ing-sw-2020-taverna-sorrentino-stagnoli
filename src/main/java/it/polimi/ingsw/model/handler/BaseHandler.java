package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.exceptions.UndeterminedSpaceException;

public abstract class BaseHandler implements RuleHandler {
    RuleHandler next;

    public BaseHandler() {}
    public BaseHandler(RuleHandler next) {
        this.next = next;
    }

    public void handleValidationRequest(ValidationContainer vc) {
        if (next != null) {
            next.handleValidationRequest(vc);
        } else {
            String exceptionalMessage = "Cannot determine validity of the followings:\n";
            exceptionalMessage += vc.getNotValidatedSpaces().toString();

            throw new UndeterminedSpaceException(exceptionalMessage);
        }
    }

    public void handle() throws Exception {
        if (next != null) {
            next.handle();
        } else {
            throw new Exception("Cannot determine correctness for the action.");
        }
    }

    public void setNext(RuleHandler next) {
        this.next = next;
    }
}
