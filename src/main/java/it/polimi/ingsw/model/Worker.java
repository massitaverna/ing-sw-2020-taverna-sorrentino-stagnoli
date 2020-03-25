package it.polimi.ingsw.model;

public class Worker {

    private Color color;
    private Coord c;
    private Board board;

    public Worker(Color c){
        this.color = c;
    }

    public void place(Coord c){
        this.c = c;
        Space position = board.getSpace(c);
        position.setOccupied();
    }

    public void move(Coord newC){
        Space position = board.getSpace(c);
        position.setUnoccupied();
        this.place(c);
    }

    // si presuppone che si sia già controllato che la posizione sia valida
    public void build(Coord c){
        Space position = board.getSpace(c);
        position.levelUp();
    }

    public Color getColor() {
        return color;
    }

    public Coord getCoord() {
        return c;
    }
}
