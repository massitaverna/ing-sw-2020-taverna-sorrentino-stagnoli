package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Turn {

    List<Coord> movableSpaces;
    Map<Level, List<Coord>> buildableSpaces;
    Map<Coord, Coord> forces;
    boolean turnEnded;

    public Turn() {
        movableSpaces = new ArrayList<>();
        buildableSpaces = new HashMap<>();
        forces = new HashMap<>();
        turnEnded = false;
    }



}
