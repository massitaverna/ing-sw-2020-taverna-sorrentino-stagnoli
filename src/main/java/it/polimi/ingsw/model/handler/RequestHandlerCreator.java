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
    private static Map<String, List<Rule>> godRules = new HashMap<>();
    private Board board;

    public RequestHandlerCreator(Board board) {
        this.god = null;
        this.board = board;
    }
    public RequestHandlerCreator(String god, Board board) {
        this.god = god;
        this.board = board;
    }

    private List<Rule> initGodRules() {
        if (godRules.containsKey(god)) {
            return godRules.get(god);
        }

        if (god.equals("Apollo")) {
            List<Rule> result = new ArrayList<>();
            Rule r = new Rule();
            r.setPurpose(Purpose.VALIDATION);
            r.setActionType(ActionType.MOVE);
            r.setDecision(Decision.GRANT);
            BiPredicate<Coord, Coord> condition = (before, after) ->
                    after.isOccupied() && before.isNear(after);
            r.setCondition(condition);
            result.add(r);
            godRules.put("Apollo", result);

            return result;
        }

        if (god.equals("Artemis")) {
            List<Rule> result = new ArrayList<>();
            Rule r = new Rule();
            r.setPurpose(Purpose.VALIDATION);
            r.setActionType(ActionType.MOVE);
            r.setDecision(Decision.GRANT);
            BiPredicate<Coord, Coord> condition = (before, after) ->
                    ;
            r.setCondition(condition);
            result.add(r);
            godRules.put("Artemis", result);

            return result;
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
                    after.isOccupied() && before.isNear(after) && Coord.sum(after, getDirection(before, after))....;
            r.setCondition(condition);
            result.add(r);
            godRules.put("Apollo", result);

            return result;
        }
    }
}
