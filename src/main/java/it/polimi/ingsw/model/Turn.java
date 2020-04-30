package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Turn implements Cloneable {

    private List<Coord> movableSpaces;
    private Map<Level, List<Coord>> buildableSpaces;
    private Map<Coord, Coord> forces;
    private boolean hasMoved;
    private boolean hasBuilt;
    private boolean turnEnded;

    public Turn() {
        movableSpaces = new ArrayList<>();
        buildableSpaces = new HashMap<>();
        forces = new HashMap<>();
        hasMoved = false;
        hasBuilt = false;
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

    public void hasMoved() {
        hasMoved = true;
    }

    public void hasBuilt() {
        hasBuilt = hasMoved;
    }

    public boolean canEndTurn() {
        return hasBuilt;
    }

    public void setEnded() {
        turnEnded = true;
    }

    public boolean hasEnded() {
        return turnEnded;
    }

    public Turn clone() {
        Turn result = new Turn();
        result.movableSpaces = getMovableSpacesCopy();
        result.buildableSpaces = getBuildableSpacesCopy();
        result.forces = getForcesCopy();
        result.hasMoved = hasMoved;
        result.hasBuilt = hasBuilt;
        result.turnEnded = turnEnded;

        return result;
    }
}
