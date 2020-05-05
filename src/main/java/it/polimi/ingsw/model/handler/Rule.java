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
import it.polimi.ingsw.model.handler.util.Pair;
import it.polimi.ingsw.model.handler.util.TriPredicate;

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


    Purpose getPurpose() {
        assert repOk();
        return this.purpose;
    }

    ActionType getActionType() {
        assert repOk();
        return actionType;
    }

    Decision getDecision() {
        assert repOk();
        return decision;
    }

    BiPredicate<Pair<Coord>, Board> getCondition() {
        assert repOk();
        return condition;
    }

    BiFunction<Coord, Coord, Coord> getForceSpaceFunction() {
        assert repOk();
        if (forceSpaceFunction != null) {
            return forceSpaceFunction;
        }
        else {
            return (a, b) -> null;
        }
    }

    Level getBuildLevel() {
        assert repOk();
        return buildLevel;
    }

    List<Rule> getGeneratedRules(Pair<Coord> oldAction) {
        List<Rule> result = new ArrayList<>();
        for (Rule rule : generatedRules) {
            if (rule.symbolicCondition != null) {
                rule.condition = (cPair, board) -> rule.symbolicCondition.test(oldAction, cPair, board);
            }
            result.add(rule);
        }
        assert repOk();
        return result;
    }

    Target getTarget() {
        assert repOk();
        return target;
    }

    void setPurpose(Purpose purpose) {
        assert this.purpose == null;
        this.purpose = purpose;
    }

    void setActionType(ActionType actionType) {
        assert this.actionType == null;
        this.actionType = actionType;
    }

    void setDecision(Decision decision) {
        assert this.decision == null;
        this.decision = decision;
    }

    void setCondition(BiPredicate<Pair<Coord>, Board> condition) {
        assert this.condition == null;
        this.condition = condition;
    }

    void setForceSpaceFunction(BiFunction<Coord, Coord, Coord> f) {
        assert this.forceSpaceFunction == null;
        this.forceSpaceFunction = f;
    }

    void setBuildLevel(Level buildLevel) {
        assert this.buildLevel == null;
        this.buildLevel = buildLevel;
    }

    void setGeneratedRules(List<Rule> generatedRules) {
        assert this.generatedRules == null;
        this.generatedRules = generatedRules;
    }

    void setSymbolicCondition(TriPredicate<Pair<Coord>, Pair<Coord>, Board> symbolicCondition) {
        assert this.symbolicCondition == null;
        this.symbolicCondition = symbolicCondition;
    }

    void setTarget(Target target) {
        assert this.target == null;
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

    private boolean repOk() {
        boolean repOk = actionType != null &&
        condition != null &&
        iff(decision == null, purpose != Purpose.VALIDATION) &&
        iff(actionType==ActionType.BUILD && purpose == Purpose.VALIDATION, buildLevel != null) &&
        ifThen(forceSpaceFunction != null, actionType == ActionType.MOVE) &&
                ifThen(forceSpaceFunction != null, decision == Decision.GRANT) &&
        iff(generatedRules == null,purpose != Purpose.GENERATION) &&
        ifThen(purpose == Purpose.VALIDATION, actionType != ActionType.END) &&
        iff(purpose == Purpose.GENERATION, target != null);

        if (!repOk) {
            System.out.println("Rep for the rule is invalid.");
        }
        return repOk;
    }

    private boolean ifThen(boolean a, boolean b) {
        return (!a||b);
    }

    private boolean iff(boolean a, boolean b) {
        return ifThen(a, b) && ifThen(b, a);
    }
}