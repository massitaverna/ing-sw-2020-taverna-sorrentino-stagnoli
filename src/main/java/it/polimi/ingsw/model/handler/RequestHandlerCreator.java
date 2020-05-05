package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.model.handler.util.Pair;
import it.polimi.ingsw.model.handler.util.TriPredicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class RequestHandlerCreator {

    private final String god;
    private static final List<Rule> standardRules = new ArrayList<>();
    private static final Map<String, List<Rule>> godRules = new HashMap<>();

    public RequestHandlerCreator() {
        this.god = null;
    }
    public RequestHandlerCreator(String god) {
        this.god = god;
    }


    public RequestHandler createHandler() {
        List<Rule> rules = createRulesList();
        RuleHandler ruleHandler = new ConcreteHandler(rules);
        RequestHandler requestHandler = new HandlerAdapter(ruleHandler);

        return requestHandler;
    }

    private List<Rule> createRulesList() {
        List<Rule> result = new ArrayList<>();
        if (god != null) {
            result.addAll(getGodRules());
        }
        result.addAll(getStandardRules());

        return result;
    }

    private static List<Rule> getStandardRules() {
        if (standardRules.isEmpty()) {
            initStandardRules();
        }
        return new ArrayList<>(standardRules);
    }

    private List<Rule> getGodRules() {

        if (!godRules.containsKey(god)) {
            initGodRules(god);
        }

        return new ArrayList<>(godRules.get(god));
    }



    // Coord before --> cPair.get(0)
    // Coord after --> cPair.get(1)

    private static void initStandardRules() {
        Rule r = new Rule();
        r.setPurpose(Purpose.VALIDATION);
        r.setDecision(Decision.GRANT);
        r.setActionType(ActionType.MOVE);
        BiPredicate<Pair<Coord>, Board> condition = (cPair, board) ->
        cPair.get(0).isNear(cPair.get(1)) && !board.getSpace(cPair.get(1)).isOccupied() &&
                !board.getSpace(cPair.get(1)).isDome() &&
                board.getSpace(cPair.get(1)).getHeight().ordinal() -
                        board.getSpace(cPair.get(0)).getHeight().ordinal() <= 1;
        r.setCondition(condition);
        standardRules.add(r);

        r = new Rule();
        r.setPurpose(Purpose.VALIDATION);
        r.setDecision(Decision.DENY);
        r.setActionType(ActionType.MOVE);
        condition = (cPair, board) ->
                !cPair.get(0).isNear(cPair.get(1)) || board.getSpace(cPair.get(1)).isOccupied() ||
                        board.getSpace(cPair.get(1)).isDome() ||
                        board.getSpace(cPair.get(1)).getHeight().ordinal() -
                                board.getSpace(cPair.get(0)).getHeight().ordinal() > 1;
        r.setCondition(condition);
        standardRules.add(r);

        //-----------------DENY BUILD on every level------------------
        r = new Rule();
        r.setPurpose(Purpose.VALIDATION);
        r.setDecision(Decision.DENY);
        r.setActionType(ActionType.BUILD);
        r.setBuildLevel(Level.GROUND);
        condition = (cPair, board) -> true;
        r.setCondition(condition);
        standardRules.add(r);

        r = new Rule();
        r.setPurpose(Purpose.VALIDATION);
        r.setDecision(Decision.DENY);
        r.setActionType(ActionType.BUILD);
        r.setBuildLevel(Level.LVL1);
        condition = (cPair, board) -> true;
        r.setCondition(condition);
        standardRules.add(r);

        r = new Rule();
        r.setPurpose(Purpose.VALIDATION);
        r.setDecision(Decision.DENY);
        r.setActionType(ActionType.BUILD);
        r.setBuildLevel(Level.LVL2);
        condition = (cPair, board) -> true;
        r.setCondition(condition);
        standardRules.add(r);

        r = new Rule();
        r.setPurpose(Purpose.VALIDATION);
        r.setDecision(Decision.DENY);
        r.setActionType(ActionType.BUILD);
        r.setBuildLevel(Level.LVL3);
        condition = (cPair, board) -> true;
        r.setCondition(condition);
        standardRules.add(r);

        r = new Rule();
        r.setPurpose(Purpose.VALIDATION);
        r.setDecision(Decision.DENY);
        r.setActionType(ActionType.BUILD);
        r.setBuildLevel(Level.DOME);
        condition = (cPair, board) -> true;
        r.setCondition(condition);
        standardRules.add(r);
        //-------------------------------------------------------------

        r = new Rule();
        r.setPurpose(Purpose.GENERATION);
        r.setTarget(Target.MYSELF);
        r.setActionType(ActionType.MOVE);
        condition = (cPair, board) -> true;
        r.setCondition(condition);
        standardRules.add(r);

        List<Rule> generatedRules = new ArrayList<>();
        r.setGeneratedRules(generatedRules);

        generatedRules.addAll(getStandardBuildUpRules());

        r = new Rule();
        r.setPurpose(Purpose.VALIDATION);
        r.setDecision(Decision.DENY);
        r.setActionType(ActionType.MOVE);
        condition = (cPair, board) -> true;
        r.setCondition(condition);
        generatedRules.add(r);

        r = new Rule();
        r.setPurpose(Purpose.GENERATION);
        r.setTarget(Target.MYSELF);
        r.setActionType(ActionType.BUILD);
        condition = (cPair, board) -> true;
        r.setCondition(condition);
        generatedRules.add(r);

        generatedRules = new ArrayList<>();
        r.setGeneratedRules(generatedRules);

        generatedRules.addAll(denyAll());
        generatedRules.add(doNothingOnEnd());

    }

    private static void initGodRules(String god) {
        List<Rule> result = new ArrayList<>();

        if (god == null) {
            throw new IllegalStateException("Tried to get god's rules without specifying god's name " +
                    "at construction-time.");
        }

        if (god.equals("Apollo")) { //COMPLETE
            Rule r = new Rule();
            r.setPurpose(Purpose.VALIDATION);
            r.setActionType(ActionType.MOVE);
            r.setDecision(Decision.GRANT);
            BiPredicate<Pair<Coord>, Board> condition = (cPair, board) ->
                    board.getSpace(cPair.get(1)).isOccupied() && cPair.get(0).isNear(cPair.get(1));
            r.setCondition(condition);
            BiFunction<Coord, Coord, Coord> forceSpaceFunction = (before, after) ->
                    before;
            r.setForceSpaceFunction(forceSpaceFunction);
            result.add(r);
        }

        if (god.equals("Artemis")) { //COMPLETE
            Rule r = new Rule();
            r.setPurpose(Purpose.GENERATION);
            r.setTarget(Target.MYSELF);
            r.setActionType(ActionType.MOVE);
            BiPredicate<Pair<Coord>, Board> condition = (cPair, board) -> true;
            r.setCondition(condition);
            result.add(r);

            List<Rule> generatedRules = new ArrayList<>();
            r.setGeneratedRules(generatedRules);
            r = new Rule();
            r.setPurpose(Purpose.VALIDATION);
            r.setDecision(Decision.DENY);
            r.setActionType(ActionType.MOVE);
            TriPredicate<Pair<Coord>, Pair<Coord>, Board> symbolicCondition = (oldPair, pair, board) ->
                    oldPair.get(0).equals(pair.get(1));
            r.setSymbolicCondition(symbolicCondition);
            generatedRules.add(r);

            generatedRules.add(standardRules.get(0));
            generatedRules.add(standardRules.get(standardRules.size() - 1));
        }

        if (god.equals("Atlas")) {
            Rule r = new Rule();
            r.setPurpose(Purpose.VALIDATION);
            r.setActionType(ActionType.BUILD);
            r.setDecision(Decision.GRANT);
            BiPredicate<Pair<Coord>, Board> condition = (cPair, board) ->
                    !board.getSpace(cPair.get(1)).isOccupied() && cPair.get(0).isNear(cPair.get(1));
            r.setCondition(condition);
            result.add(r);
        }

        /*if (god.equals("Demeter")) {
            List<Rule> result = new ArrayList<>();
            Rule r = new Rule();
            r.setPurpose(Purpose.VALIDATION);
            r.setActionType(ActionType.BUILD);
            r.setDecision(Decision.GRANT);
            BiPredicate<Pair<Coord>, Board> condition = (cPair, board) ->
                    !board.getSpace(cPair.get(1)).isOccupied() && cPair.get(0).isNear(cPair.get(1));
            r.setCondition(condition);
            result.add(r);
            godRules.put("Demeter", result);
        }*/

        if (god.equals("Hephaestus")) {
            Rule r = new Rule();
            r.setPurpose(Purpose.VALIDATION);
            r.setActionType(ActionType.BUILD);
            r.setDecision(Decision.GRANT);
            BiPredicate<Pair<Coord>, Board> condition = (cPair, board) ->
                    !board.getSpace(cPair.get(1)).isOccupied() && cPair.get(0).isNear(cPair.get(1));
            r.setCondition(condition);
            result.add(r);
        }

        if (god.equals("Minotaur")) { //COMPLETE
            Rule r = new Rule();
            r.setPurpose(Purpose.VALIDATION);
            r.setActionType(ActionType.MOVE);
            r.setDecision(Decision.GRANT);
            BiFunction<Coord, Coord, Coord> getDirection = (before, after) ->
                    new Coord(after.x - before.x, after.y - before.y);
            BiPredicate<Pair<Coord>, Board> condition = (cPair, board) ->
                    board.getSpace(cPair.get(1)).isOccupied() && cPair.get(0).isNear(cPair.get(1)) &&
                            Coord.validCoord(cPair.get(1).sum(getDirection.apply(cPair.get(0), cPair.get(1)))) &&
                            !board.getSpace(cPair.get(1).sum(getDirection.apply(cPair.get(0), cPair.get(1)))).isOccupied();
            r.setCondition(condition);
            BiFunction<Coord, Coord, Coord> forceSpaceFunction = (before, after) ->
                    after.sum(getDirection.apply(before, after));
            r.setForceSpaceFunction(forceSpaceFunction);
            result.add(r);
        }

        godRules.put(god, result);
    }


    private static List<Rule> getStandardBuildUpRules() {
        List<Rule> result = new ArrayList<>();

        BiPredicate<Pair<Coord>, Board> conditionOnProximity = (cPair, board) ->
                cPair.get(0).isNear((cPair.get(1)));
        BiPredicate<Pair<Coord>, Board> conditionOnFreeSpace = (cPair, board) ->
                !board.getSpace(cPair.get(1)).isOccupied() &&
                        !board.getSpace(cPair.get(1)).isDome();
        BiPredicate<Pair<Coord>, Board> conditionOnLevelUp;

        BiPredicate<Pair<Coord>, Board> condition;
        Rule r;

        for (Level level : Level.values()) {

            if (level == Level.GROUND) {
                r = new Rule();
                r.setPurpose(Purpose.VALIDATION);
                r.setActionType(ActionType.BUILD);
                r.setDecision(Decision.DENY);
                r.setBuildLevel(level);
                r.setCondition((a, b) -> true);
                result.add(r);
                continue;
            }

            r = new Rule();
            r.setPurpose(Purpose.VALIDATION);
            r.setActionType(ActionType.BUILD);
            r.setDecision(Decision.GRANT);
            r.setBuildLevel(level);
            conditionOnLevelUp = (cPair, board) ->
                    board.getSpace(cPair.get(1)).getHeight().ordinal() == level.ordinal() - 1;
            condition =
                    conditionOnProximity
                            .and(conditionOnFreeSpace
                                    .and(conditionOnLevelUp));
            r.setCondition(condition);
            result.add(r);

            r = new Rule();
            r.setPurpose(Purpose.VALIDATION);
            r.setActionType(ActionType.BUILD);
            r.setDecision(Decision.DENY);
            r.setBuildLevel(level);
            r.setCondition(condition.negate());
            result.add(r);

        }

        return result;
    }

    private static List<Rule> denyAll() {
        List<Rule> result = new ArrayList<>();
        Rule r;
        BiPredicate<Pair<Coord>, Board> condition;

        r = new Rule();
        r.setPurpose(Purpose.VALIDATION);
        r.setDecision(Decision.DENY);
        r.setActionType(ActionType.MOVE);
        condition = (cPair, board) -> true;
        r.setCondition(condition);
        result.add(r);

        r = new Rule();
        r.setPurpose(Purpose.VALIDATION);
        r.setDecision(Decision.DENY);
        r.setActionType(ActionType.BUILD);
        r.setBuildLevel(Level.GROUND);
        condition = (cPair, board) -> true;
        r.setCondition(condition);
        result.add(r);

        r = new Rule();
        r.setPurpose(Purpose.VALIDATION);
        r.setDecision(Decision.DENY);
        r.setActionType(ActionType.BUILD);
        r.setBuildLevel(Level.LVL1);
        condition = (cPair, board) -> true;
        r.setCondition(condition);
        result.add(r);

        r = new Rule();
        r.setPurpose(Purpose.VALIDATION);
        r.setDecision(Decision.DENY);
        r.setActionType(ActionType.BUILD);
        r.setBuildLevel(Level.LVL2);
        condition = (cPair, board) -> true;
        r.setCondition(condition);
        result.add(r);

        r = new Rule();
        r.setPurpose(Purpose.VALIDATION);
        r.setDecision(Decision.DENY);
        r.setActionType(ActionType.BUILD);
        r.setBuildLevel(Level.LVL3);
        condition = (cPair, board) -> true;
        r.setCondition(condition);
        result.add(r);

        r = new Rule();
        r.setPurpose(Purpose.VALIDATION);
        r.setDecision(Decision.DENY);
        r.setActionType(ActionType.BUILD);
        r.setBuildLevel(Level.DOME);
        condition = (cPair, board) -> true;
        r.setCondition(condition);
        result.add(r);

        return result;
    }

    private static Rule doNothingOnEnd() {
        Rule r = new Rule();
        r.setPurpose(Purpose.GENERATION);
        r.setTarget(Target.MYSELF);
        r.setActionType(ActionType.END);
        BiPredicate<Pair<Coord>, Board> condition = (cPair, board) -> true;
        r.setCondition(condition);

        List<Rule> generatedRules = new ArrayList<>();
        r.setGeneratedRules(generatedRules);

        generatedRules.addAll(denyAll());

        return r;
    }
}
