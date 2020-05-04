package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.model.InvalidCoordinatesException;

public class Coord {
    public final int x;
    public final int y;

    public Coord(int x, int y){
        this.x=x;
        this.y=y;
    }

    public static boolean validCoord(Coord c) {
        if(c.x < 0 || c.x > 4 || c.y < 0 || c.y > 4) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean isNear(Coord c) throws InvalidCoordinatesException {
        if( !Coord.validCoord(this) ){
            throw new InvalidCoordinatesException("Mehtod isNear() has been called on invalid coordinates.");
        }
        if( !Coord.validCoord(c) ){
            throw new InvalidCoordinatesException("Invalid coordinates given.");
        }

        //if it's the same coordinate, return false
        if(this.x == c.x && this.y == c.y)
            return false;

        int diffX = Math.abs(this.x - c.x);
        int diffY = Math.abs(this.y - c.y);
        if (diffX > 1 || diffY > 1){
            return false;
        }
        else {
            return true;
        }
    }

    public Coord sum(Coord c) {
        return new Coord(this.x + c.x, this.y + c.y);
    }

    public static Coord convertStringToCoord(String input) {
        input = input.toUpperCase();
        int x = (input.charAt(0) - 'A');
        int y = (input.charAt(1) - '1');

        return new Coord(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Coord){
            Coord other = (Coord)obj;
            return (this.x == other.x && this.y == other.y);
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")" ;
    }
}
