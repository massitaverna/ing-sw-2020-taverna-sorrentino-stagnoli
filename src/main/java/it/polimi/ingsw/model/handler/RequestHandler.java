package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.Level;

import java.util.List;
import java.util.Map;

public interface RequestHandler {

    public void getValidSpaces(Coord current, List<Coord> allSpaces,
                               List<Coord> movableSpaces, Map<Level, List<Coord>> buildableSpaces,
                               Map<Coord, Coord> forces);

    public void generate();

}
