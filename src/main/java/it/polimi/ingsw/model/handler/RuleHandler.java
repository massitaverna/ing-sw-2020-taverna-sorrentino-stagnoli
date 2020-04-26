package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.Coord;

interface RuleHandler {
    void handleValidationRequest(ValidationContainer vc);
    void generate(Coord before, Coord after, ActionType at);
}
