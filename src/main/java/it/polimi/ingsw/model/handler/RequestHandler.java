package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.model.Space;

import java.util.List;
import java.util.Map;

public interface RequestHandler {

    public void getValidSpaces(Space current, List<Space> allSpaces,
                               List<Space> movableSpaces, Map<Level, List<Space>> buildableSpaces,
                               Map<Space, Space> forces);

    public void generate();

}
