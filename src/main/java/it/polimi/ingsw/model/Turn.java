package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Turn {

    private List<Coord> movableSpaces;
    private Map<Level, List<Coord>> buildableSpaces;
    private Map<Coord, Coord> forces;
    private boolean turnEnded;

    public Turn() {
        movableSpaces = new ArrayList<>();
        buildableSpaces = new HashMap<>();
        forces = new HashMap<>();
        turnEnded = false;
    }

    public List<Coord> getMovableSpacesReference() {
        return movableSpaces;
    }

    public Map<Level, List<Coord>> getBuildableSpacesReference() {
        return buildableSpaces;
    }

    public Map<Coord, Coord> getForcesReference() {
        return forces;
    }

    public List<Coord> getMovableSpacesCopy() {
        return new ArrayList<>(movableSpaces);
    }

    public Map<Level, List<Coord>> getBuildableSpacesCopy() {
        return new HashMap<>(buildableSpaces);
    }

    public Map<Coord, Coord> getForcesCopy() {
        return new HashMap<>(forces);
    }
}
