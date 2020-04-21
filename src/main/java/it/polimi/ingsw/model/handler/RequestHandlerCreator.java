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


    private List<Rule> getGodRules() {

        if (!godRules.containsKey(god)) {
            initGodRules();
        }

        return godRules.get(god);
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
            godRules.put("Apollo", result);
        }
    }
}
