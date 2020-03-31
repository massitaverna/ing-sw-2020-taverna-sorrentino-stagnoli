package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidCoordinatesException;

public class Coord {
    public int x;
    public int y;

    public Coord(int x, int y){
        this.x=x;
        this.y=y;
    }

    public static boolean validCoord(Coord c) throws InvalidCoordinatesException {
        if(c.x < 0 || c.x > 4 || c.y < 0 || c.y > 4) {
            throw new InvalidCoordinatesException("Coordinates x & y must be between 0 and 4.");
        }
        else{
            return true;
        }
    }

    public boolean isNear(Coord c) throws InvalidCoordinatesException {
        if(Coord.validCoord(this));
        if(Coord.validCoord(c));

        int diffX = Math.abs(this.x - c.x);
        int diffY = Math.abs(this.y - c.y);
        if(diffX > 1 || diffY > 1){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Coord){
            Coord other = (Coord)obj;
            return (this.x == other.x && this.y == other.y);
        }
        return false;
    }
}
