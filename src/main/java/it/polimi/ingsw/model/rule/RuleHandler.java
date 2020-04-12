package it.polimi.ingsw.model.rule;

import it.polimi.ingsw.exceptions.UnhandledActionException;

public interface RuleHandler {
    public void handle() throws UnhandledActionException;
}
