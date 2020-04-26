/*
Rep:

actionType != null
condition != null
decision == null <==> purpose != VALIDATION
actionType==BUILD <==> buildLevel != null
forceDestination != null ==> actionType == MOVE
forceDestination != null ==> decision == GRANT
generatedRules == null <==> purpose != GENERATION
 */



package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.model.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class Rule {
    private Purpose purpose;
    private ActionType actionType;
    private Decision decision;
    private BiPredicate<Coord, Coord> condition;
    private BiFunction<Coord, Coord, Coord> forceSpaceFunction;
    private Level buildLevel;
    private List<Rule> generatedRules;

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
    BiPredicate<Coord, Coord> getCondition() {
        return condition;
    }

    BiFunction<Coord, Coord, Coord> getForceSpaceFunction() {
        return forceSpaceFunction;
    }

    Level getBuildLevel() {
        return buildLevel;
    }

    List<Rule> getGeneratedRules() {
        return new ArrayList<>(generatedRules);
    }

    void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    void setDecision(Decision decision) {
        this.decision = decision;
    }

    void setCondition(BiPredicate<Coord, Coord> condition) {
        this.condition = condition;
    }

    void setForceSpaceFunction(BiFunction<Coord, Coord, Coord> f) {
        this.forceSpaceFunction = f;
    }

    void setBuildLevel(Level buildLevel) {
        this.buildLevel = buildLevel;
    }

    void setGeneratedRules(List<Rule> generatedRules) {
        this.generatedRules = new ArrayList<>(generatedRules);
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
