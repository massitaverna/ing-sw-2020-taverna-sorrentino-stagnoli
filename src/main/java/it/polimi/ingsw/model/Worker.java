/*
Note:
A clone of a worker contains a different instance of Player player.
This player has only the same name as the original one, but nothing else.
 */

package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.model.WorkerNotFoundException;

import java.io.Serializable;

public class Worker implements Cloneable, Serializable {

    private static final long serialVersionUID = 3L;

    private Color color;
    private Coord position;
    private Player player;

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

    public Worker clone() {

        Worker result = new Worker(new Player(player.getNickname()));
        result.color = color;
        result.position = position;
        return result;
    }
}
