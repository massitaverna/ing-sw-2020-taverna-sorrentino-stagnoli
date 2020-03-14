package it.polimi.ingsw.Model;

public class Coord {
    public int x, y;

    public Coord(int x, int y){
        this.x=x;
        this.y=y;
    }

    @Override
    public String toString(){
        String s = "X: " + this.x + " Y: " + this.y;
        return s;
    }
}
