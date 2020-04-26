package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.Level;

import java.util.List;
import java.util.Map;

class HandlerAdapter implements RequestHandler {

    RuleHandler ruleHandler;
    Coord before;

    HandlerAdapter(RuleHandler ruleHandler) {
        this.ruleHandler = ruleHandler;
    }

    @Override
    public void getValidSpaces(Coord current, List<Coord> allSpaces,
                               List<Coord> movableSpaces, Map<Level, List<Coord>> buildableSpaces,
                               Map<Coord, Coord> forces) {

        ValidationContainer validationContainer = new ValidationContainer(current, allSpaces);

        ruleHandler.handleValidationRequest(validationContainer);


        movableSpaces.addAll(validationContainer.getMovableSpaces());
        buildableSpaces.putAll(validationContainer.getBuildableSpaces());
        forces.putAll(validationContainer.getForces());

        before = validationContainer.getCurrentPosition();
    }

    @Override
    public void generate(Coord after, ActionType at) {

        ruleHandler.generate(before, after, at);
    }
}
