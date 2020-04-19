package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.model.Space;

import java.util.List;
import java.util.Map;

class HandlerAdapter implements RequestHandler {

    RuleHandler ruleHandler;

    HandlerAdapter(RuleHandler ruleHandler) {
        this.ruleHandler = ruleHandler;
    }

    @Override
    public void getValidSpaces(Space current, List<Space> allSpaces,
                               List<Space> movableSpaces, Map<Level, List<Space>> buildableSpaces,
                               Map<Space,Space> forces) {

        ValidationContainer validationContainer = new ValidationContainer(current, allSpaces);

        ruleHandler.handleValidationRequest(validationContainer);


        movableSpaces.addAll(validationContainer.getMovableSpaces());
        buildableSpaces.putAll(validationContainer.getBuildableSpaces());
        forces.putAll(validationContainer.getForces());
    }

    @Override
    public void generate() {

    }
}
