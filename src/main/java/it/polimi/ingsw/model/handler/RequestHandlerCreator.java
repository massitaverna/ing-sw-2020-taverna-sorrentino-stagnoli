package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.Space;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class RequestHandlerCreator {

    private final String god;
    private static List<Rule> standardRules = new ArrayList<>();
    private static Map<String, List<Rule>> godRules = new HashMap<>();
    private Board board;

    public RequestHandlerCreator(Board board) {

        //TODO: da sistemare: non posso passare qui la board, serve la board
        // a tempo di valutazione del BiPredicate.
        // Idea: BiPredicate<Pair<Coord, Space>, Pair<Coord, Space>>
        this.god = null;
        this.board = board;
    }
    public RequestHandlerCreator(String god, Board board) {
        this.god = god;
        this.board = board;
    }


    public RequestHandler createHandler() {
        List<Rule> rules = new ArrayList<>();
        rules.addAll(getGodRules());
        rules.addAll(getStandardRules());

        RuleHandler ruleHandler = new ConcreteHandler(rules);
        RequestHandler requestHandler = new HandlerAdapter(ruleHandler);

        return requestHandler;
    }
    private static List<Rule> getStandardRules() {
        if (standardRules.isEmpty()) {
            initStandardRules();
        }
        return standardRules;
    }

    private List<Rule> getGodRules() {

        if (!godRules.containsKey(god)) {
            initGodRules();
        }

        return godRules.get(god);
    }


    private static void initStandardRules() {
        //To be implemented
    }

    private void initGodRules() {

        if (god == null) {
            throw new IllegalStateException("Tried to get god's rules without specifying god's name " +
                    "at construction-time.");
        }

        if (god.equals("Apollo")) {
            List<Rule> result = new ArrayList<>();
            Rule r = new Rule();
            r.setPurpose(Purpose.VALIDATION);
            r.setActionType(ActionType.MOVE);
            r.setDecision(Decision.GRANT);
            BiPredicate<Coord, Coord> condition = (before, after) ->
                    board.getSpace(after).isOccupied() && before.isNear(after);
            r.setCondition(condition);
            BiFunction<Coord, Coord, Coord> forceSpaceFunction = (before, after) ->
                    before;
            r.setForceSpaceFunction(forceSpaceFunction);
            result.add(r);
            godRules.put("Apollo", result);
        }

        if (god.equals("Artemis")) {
            List<Rule> result = new ArrayList<>();
            Rule r = new Rule();
            r.setPurpose(Purpose.VALIDATION);
            r.setActionType(ActionType.MOVE);
            r.setDecision(Decision.GRANT);
            BiPredicate<Coord, Coord> condition = null; //(before, after) -> ...; // To be implemented
            r.setCondition(condition);
            result.add(r);
            godRules.put("Artemis", result);
        }*/

        if (god.equals("Atlas")) {
            List<Rule> result = new ArrayList<>();
            Rule r = new Rule();
            r.setPurpose(Purpose.VALIDATION);
            r.setActionType(ActionType.BUILD);
            r.setDecision(Decision.GRANT);
            BiPredicate<Coord, Coord> condition = (before, after) ->
                    !board.getSpace(after).isOccupied() && before.isNear(after);
            r.setCondition(condition);
            result.add(r);
            godRules.put("Atlas", result);
        }

        /*if (god.equals("Demeter")) {
            List<Rule> result = new ArrayList<>();
            Rule r = new Rule();
            r.setPurpose(Purpose.VALIDATION);
            r.setActionType(ActionType.BUILD);
            r.setDecision(Decision.GRANT);
            BiPredicate<Coord, Coord> condition = (before, after) ->
                    !board.getSpace(after).isOccupied() && before.isNear(after);
            r.setCondition(condition);
            result.add(r);
            godRules.put("Demeter", result);
        }*/

        if (god.equals("Hephastus")) {
            List<Rule> result = new ArrayList<>();
            Rule r = new Rule();
            r.setPurpose(Purpose.VALIDATION);
            r.setActionType(ActionType.BUILD);
            r.setDecision(Decision.GRANT);
            BiPredicate<Coord, Coord> condition = (before, after) ->
                    !board.getSpace(after).isOccupied() && before.isNear(after);
            r.setCondition(condition);
            result.add(r);
            godRules.put("Hephastus", result);
        }

        if (god.equals("Minotaur")) {
            List<Rule> result = new ArrayList<>();
            Rule r = new Rule();
            r.setPurpose(Purpose.VALIDATION);
            r.setActionType(ActionType.MOVE);
            r.setDecision(Decision.GRANT);
            BiFunction<Coord, Coord, Coord> getDirection = (before, after) ->
                    new Coord(after.x -before.x, after.y - before.y);
            BiPredicate<Coord, Coord> condition = (before, after) ->
                    board.getSpace(after).isOccupied() && before.isNear(after) &&
                            Coord.validCoord(after.sum(getDirection.apply(before, after))) &&
                            !board.getSpace(after.sum(getDirection.apply(before, after))).isOccupied();
            r.setCondition(condition);
            // va settata la forzatura
            result.add(r);
            godRules.put("Minotaur", result);
        }
    }
}
