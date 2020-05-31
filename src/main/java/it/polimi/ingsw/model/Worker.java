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

    @Override
    public Worker clone() {
        Worker result = new Worker(new Player(player.getNickname()));
        result.color = color;
        if(this.position != null) {
            result.position = new Coord(position.x, position.y);
        }
        return result;
    }

    @Override
    public boolean equals (Object o) {
        if (!(o instanceof Worker)) return false;
        Worker that = (Worker) o;
        if (this.position != null) {
            return this.color == that.color &&
                    this.position == that.position &&
                    this.player.getNickname().equals(that.player.getNickname());
        }

        else {
            return super.equals(o);
        }
    }
}
