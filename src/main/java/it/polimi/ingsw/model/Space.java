package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.model.SpaceFullException;
import it.polimi.ingsw.exceptions.model.SpaceOccupiedException;

public class Space {
    private boolean occupied;
    private boolean hasDome;
    private Level height;

    public void setOccupied(){
        occupied = true;
    }

    public void setUnoccupied(){
        occupied = false;
    }

    public void setDome(){
        hasDome = true;
    }

    public boolean isOccupied(){
        return occupied;
    }

    public boolean isDome(){
        return this.hasDome;
    }

    public Level getHeight(){
        return height;
    }

    //eccezione se viene invocata su Space con cupola o player
    public void levelUp() throws SpaceFullException, SpaceOccupiedException {
        if(this.isOccupied()){
            throw new SpaceOccupiedException("This space is occupied.");
        }

        if(this.height != Level.LVL3) {
            height = Level.values()[height.ordinal() + 1];
        }
        else
            if(!this.hasDome){
                this.hasDome = true;
            }
            else {
                throw new SpaceFullException("This space is full (DOME).");
            }
    }
}
