/*
Rep:

purpose == VALIDATION <==> actionType != null
condition == null <==> actionType==BUILD
conditionsOnLevels == null <==> actionType!=BUILD
validationType == null <==> purpose != VALIDATION
actionType==BUILD <==> buildLevel != null
forceDestination != null ==> actionType == MOVE
forceDestination != null ==> decision == GRANT
 */



package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.model.Space;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class Rule {
    private Purpose purpose;
    private ActionType actionType;
    private Decision decision;
    private BiPredicate<Space, Space> condition;
    private BiFunction<Space, Space, Space> forceSpaceFunction;
    private Level buildLevel;

    Purpose getPurpose() {
        return this.purpose;
    }

    ActionType getActionType() {
        if (purpose != Purpose.VALIDATION) {
            throw new IllegalStateException("Tried to check actionType on " + this);
        }

        return actionType;
    }

    Decision getDecision() {
        return decision;
    }

    //Used for MoveRules or for GenerationRules
    BiPredicate<Space, Space> getCondition() {
        return condition;
    }

    BiFunction<Space, Space, Space> getForceSpaceFunction() {
        return forceSpaceFunction;
    }

    Level getBuildLevel() {
        return buildLevel;
    }


    @Override
    public String toString() {
        String result;

        if (actionType != null) {
            result = actionType.name();
        } else {
            result = "";
        }

        result = purpose.name() + result + "rule" + super.toString();
        return result;
    }
}
