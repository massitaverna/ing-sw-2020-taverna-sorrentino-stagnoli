package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.Level;

import java.util.List;
import java.util.Map;

public interface RequestHandler {

    void getValidSpaces(Coord current, Board board,
                               List<Coord> movableSpaces, Map<Level, List<Coord>> buildableSpaces,
                               Map<Coord, Coord> forces);

    void generate(Coord after, ActionType at);
    boolean checkForWin(Coord after, ActionType at);
    void reset();

}
