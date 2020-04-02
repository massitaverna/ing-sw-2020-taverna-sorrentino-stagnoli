package it.polimi.ingsw.model;

public class Worker {

    private Color color;
    private Coord position;
    private Player player;

    // Serve davvero che un worker sappia chi e' il suo player?
    public Worker(Player p){
        this.player = p;
        this.position.x = -1;
        this.position.y = -1;
    }

    /*public void place(Coord c){
        //TODO: controllare che lo space in posizione c sia disoccupato
        this.position = c;
        Space position = board.getSpace(c);
        position.setOccupied();
    }

    public void move(Coord newC){
        Space currentPosition = board.getSpace(position);
        currentPosition.setUnoccupied();
        this.place(newC);
    }

    // si presuppone che si sia già controllato che la posizione sia valida
    public void build(Coord c){
        //TODO: controllare che lo space in c sia costruibile
        Space position = board.getSpace(c);
        position.levelUp();
    }*/

    public void setPosition(Coord newPos){
        //TODO: controllare che newPos sia valido
        this.position = newPos;
    }

    public Coord getPosition(){
        return  this.position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color c){
        this.color = c;
    }

    public String getPlayerNickname(){
        return this.player.getNickname();
    }
}
