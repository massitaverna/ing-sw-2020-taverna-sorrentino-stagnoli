package it.polimi.ingsw.model;

public class Worker {

    private Color color;
    private int x;
    private int y;
    private Board board;

    public void place(int x, int y){
        this.x = x;
        this.y = y;
        Space position = board.getSpace(x, y);
        position.setOccupied();
    }

    public Space getPosition(){
        return board.getSpace(x, y);
    }

    public void move(int newX, int newY){
        Space position = board.getSpace(this.x, this.y);
        position.setUnoccupied();
        this.place(newX, newY);
    }

    // si presuppone che si sia già controllato che la posizione sia valida
    public void build(int x, int y){
        Space position = board.getSpace(x, y);
        position.levelUp();
    }

    public int getX(){
        return x;
    }

    public Color getColor() {
        return color;
    }

    public int getY(){
        return y;
    }
}
