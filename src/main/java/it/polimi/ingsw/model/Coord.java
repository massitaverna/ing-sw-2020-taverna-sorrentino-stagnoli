package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.model.InvalidCoordinatesException;

import java.io.Serializable;

public class Coord implements Serializable {

    private static final long serialVersionUID = 1L;

    public final int x;
    public final int y;

    public Coord(int x, int y){
        this.x=x;
        this.y=y;
    }

    /**
     * Check if the given coordinates are valid for the game
     * @param c coordinates to check
     * @return true if valid, false otherwise
     */
    public static boolean validCoord(Coord c) {
        if(c.x < 0 || c.x > 4 || c.y < 0 || c.y > 4) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Check if this coordinates are near to the given coordinates (1 step horizontally, vertically or both)
     * @param c the other given coordinates
     * @return true if near, false otherwise
     * @throws InvalidCoordinatesException when this or the given coordinates are not valid
     */
    public boolean isNear(Coord c) throws InvalidCoordinatesException {
        if( !Coord.validCoord(this) ){
            throw new InvalidCoordinatesException("Method isNear() has been called on invalid coordinates.");
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

    /**
     * Sum the components of the given coordinates to this
     * @param c the coordinates to sum
     * @return new coordinates which are the sum
     */
    public Coord sum(Coord c) {
        return new Coord(this.x + c.x, this.y + c.y);
    }

    /**
     * Convert a string (like A1, B2...) to a Coord object
     * @param input the string to convert
     * @return a Coord object equivalent
     * @throws IllegalArgumentException when bad input
     */
    public static Coord convertStringToCoord(String input) throws IllegalArgumentException {
        if (input.length() != 2)
            throw new IllegalArgumentException();
        input = input.toUpperCase();
        int y = (input.charAt(0) - 'A');
        int x = (input.charAt(1) - '1');

        Coord result = new Coord(x, y);
        if (!validCoord(result)) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    /**
     * Equality check between coordinates
     * @param obj the other coordinates
     * @return true if the components has the same values, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Coord){
            Coord other = (Coord) obj;
            return (this.x == other.x && this.y == other.y);
        }
        return false;
    }

    /**
     * ToString method
     * @return
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")" ;
    }

    /**
     * HashCode method
     * @return
     */
    @Override
    public int hashCode() {
        return 10*x + y;
    }
}
