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

    public void setOccupied(){
        occupied = true;
    }

    public void setUnoccupied(){
        occupied = false;
    }

    private void setDome(){
        hasDome = true;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public boolean isDome(){
        return this.hasDome;
    }

    public Level getHeight(){
        return height;
    }

    public void setLevel(Level level) {


        //Not sure this check should remain
        if(this.isDome()){
            throw new SpaceOccupiedException("This space contains a dome.");
        }

        if (level != Level.DOME) {
            height = level;
        } else {
            setDome();
        }
    }

    //eccezione se viene invocata su Space con cupola o player
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

    @Override
    public Space clone() {
        Space result = new Space();
        result.occupied = occupied;
        result.hasDome = hasDome;
        result.height = height;

        return result;
    }

    @Override
    public String toString() {
        String result = height.toString() + (hasDome ? " (DOME)" : "") +
                (occupied ? " " : " un") + "occupied";
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Space)) return false;
        Space that = (Space) o;
        return this.height == that.height &&
                this.occupied == that.occupied &&
                this.hasDome == that.hasDome;
    }
}
