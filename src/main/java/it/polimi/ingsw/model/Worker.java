package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.model.WorkerNotFoundException;

public class Worker {

    private Color color;
    private Coord position;
    private Player player;

    // Serve davvero che un worker sappia chi e' il suo player?
    public Worker(Player p){
        this.player = p;
        this.position = null;
    }

    public void setPosition (Coord newPos) throws IllegalArgumentException {

        //controllare che newPos sia valido
        if (!Coord.validCoord(newPos)) {
            throw new IllegalArgumentException("Invalid Coordinates");
        }

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
