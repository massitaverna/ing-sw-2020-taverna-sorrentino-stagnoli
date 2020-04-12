package it.polimi.ingsw.model.rule;

import it.polimi.ingsw.exceptions.UnhandledActionException;

public class StandardHandler extends BaseHandler {

    public StandardHandler() {}
    public StandardHandler(RuleHandler next) {
        super(next);
    }
    public void handle() throws UnhandledActionException {
        // Do standard checks
        // If (cannot resolve decidability) {
            super.handle();
        // }
    }
}
