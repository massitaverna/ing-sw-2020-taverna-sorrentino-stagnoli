package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.model.SpaceFullException;
import it.polimi.ingsw.exceptions.model.SpaceOccupiedException;

import java.io.Serializable;

public class Space implements Cloneable, Serializable {

    private static final long serialVersionUID = 4L;

    private boolean occupied;
    private boolean hasDome;
    private Level height;

    public Space() {
        occupied = false;
        hasDome = false;
        height = Level.GROUND;
    }

    /**
     * Set this space as occupied
     */
    public void setOccupied(){
        occupied = true;
    }

    /**
     * Set this space as unoccupied
     */
    public void setUnoccupied(){
        occupied = false;
    }

    /**
     * set this space as containing a dome
     */
    private void setDome(){
        hasDome = true;
    }

    /**
     * Check if this space is occupied
     * @return
     */
    public boolean isOccupied() {
        return occupied;
    }

    /**
     * Check if there is a dome on this space
     * @return
     */
    public boolean isDome(){
        return this.hasDome;
    }

    /**
     * Get the current level of this space
     * @return the Level of this space
     */
    public Level getHeight(){
        return height;
    }

    /**
     * Set a Level for this space
     * @param level the Level to set
     */
    public void setLevel(Level level) {


        //Not sure this check should remain
        if(this.isDome()){
            throw new SpaceFullException("This space contains a dome.");
        }

        if (level != Level.DOME) {
            height = level;
        } else {
            setDome();
        }
    }

    /**
     * Increase the level of this space
     * @throws SpaceFullException when the space is full
     * @throws SpaceOccupiedException when the space is occupied
     */
    public void levelUp() throws SpaceFullException, SpaceOccupiedException {
        if(this.isOccupied()){
            throw new SpaceOccupiedException("This space is occupied.");
        }

        if(this.isDome()){
            throw new SpaceOccupiedException("This space contains a dome.");
        }

        //if not level 3, level up
        if(this.height != Level.LVL3) {
            height = Level.values()[height.ordinal() + 1];
        }
        //if level 3, set dome
        else {
            this.setDome();
        }
    }

    /**
     * Clone
     * @return
     */
    @Override
    public Space clone() {
        Space result = new Space();
        result.occupied = occupied;
        result.hasDome = hasDome;
        result.height = height;

        return result;
    }

    /**
     * ToString
     * @return
     */
    @Override
    public String toString() {
        String result = height.toString() + (hasDome ? " (DOME)" : "") +
                (occupied ? " " : " un") + "occupied";
        return result;
    }

    /**
     * Equals
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Space)) return false;
        Space that = (Space) o;
        return this.height == that.height &&
                this.occupied == that.occupied &&
                this.hasDome == that.hasDome;
    }
}
