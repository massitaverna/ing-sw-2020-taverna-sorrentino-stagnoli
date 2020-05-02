/*
Rep:

actionType != null
condition != null
decision == null <==> purpose != VALIDATION
actionType==BUILD <==> buildLevel != null
forceDestination != null ==> actionType == MOVE
forceDestination != null ==> decision == GRANT
generatedRules == null <==> purpose != GENERATION
purpose == VALIDATION ==> actionType != END
purpose == GENERATION <==> target != null
 */



package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class Rule {
    private Purpose purpose;
    private ActionType actionType;
    private Decision decision;
    private BiPredicate<Pair<Coord>, Board> condition;
    private BiFunction<Coord, Coord, Coord> forceSpaceFunction;
    private Level buildLevel;
    private List<Rule> generatedRules;
    private Target target;
    private TriPredicate<Pair<Coord>, Pair<Coord>, Board> symbolicCondition;

    public Rule() {
        forceSpaceFunction = (c1, c2) -> null;
    }

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

    BiPredicate<Pair<Coord>, Board> getCondition() {
        return condition;
    }

    BiFunction<Coord, Coord, Coord> getForceSpaceFunction() {
        return forceSpaceFunction;
    }

    Level getBuildLevel() {
        return buildLevel;
    }

    List<Rule> getGeneratedRules(Pair<Coord> oldAction) {
        List<Rule> result = new ArrayList<>();
        for (Rule rule : generatedRules) {
            if (rule.symbolicCondition != null) {
                rule.condition = (cPair, board) -> symbolicCondition.test(oldAction, cPair, board);
            }
            result.add(rule);
        }
        return result;
    }

    Target getTarget() {
        return target;
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

    void setCondition(BiPredicate<Pair<Coord>, Board> condition) {
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

    void setSymbolicCondition(TriPredicate<Pair<Coord>, Pair<Coord>, Board> symbolicCondition) {
        this.symbolicCondition = symbolicCondition;
    }

    void setTarget(Target target) {
        this.target = target;
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