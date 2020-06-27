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
    private final Player player;

    public Worker(Player p){
        this.player = p;
        this.position = null;
    }

    /**
     * Set the position coordinates of this worker
     * @param newPos The coordinates to set
     * @throws IllegalArgumentException when coordinates are invalid
     */
    public void setPosition (Coord newPos) throws IllegalArgumentException {

        if (!Coord.validCoord(newPos)) {
            throw new IllegalArgumentException("Invalid Coordinates");
        }

        this.position = newPos;
    }

    /**
     * Get the current position of this worker
     * @return
     */
    public Coord getPosition(){
        return  this.position;
    }

    /**
     * Get the color of this worker
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     * Set the color for this worker
     * @param c the color to set
     */
    public void setColor(Color c){
        this.color = c;
    }

    /**
     * Get the nickname of the player to which this worker belongs
     * @return
     */
    public String getPlayerNickname(){
        return this.player.getNickname();
    }

    public String getGod() {
        return player.getGod().getName();
    }

    /**
     * Clone
     * @return
     */
    @Override
    public Worker clone() {
        Player owner = new Player(player.getNickname());
        owner.setGod(player.getGod());
        Worker result = new Worker(owner);
        result.color = color;
        if(this.position != null) {
            result.position = new Coord(position.x, position.y);
        }
        return result;
    }

    /**
     * Equals
     * @param o
     * @return
     */
    @Override
    public boolean equals (Object o) {
        if (!(o instanceof Worker)) return false;
        Worker that = (Worker) o;
        if (this.position != null) {
            return this.color == that.color &&
                    this.position.equals(that.position) &&
                    this.player.getNickname().equals(that.player.getNickname());
        }

        else {
            return super.equals(o);
        }
    }

    @Override
    public String toString() {
        return "[" + color + ", " + position + ", " + player.getNickname() + "]";
    }
}
