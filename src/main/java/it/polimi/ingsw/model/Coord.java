package it.polimi.ingsw.model;

public class Coord {
    public int x;
    public int y;

    public Coord(int x, int y){
        this.x=x;
        this.y=y;
    }

    public static boolean isValidCoord(Coord c){
        if(c.x < 0 || c.x > 4 || c.y < 0 || c.y > 4) {
            return false;
        }
        return true;
    }
}
