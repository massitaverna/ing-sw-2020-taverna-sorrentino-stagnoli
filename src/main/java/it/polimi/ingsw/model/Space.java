package it.polimi.ingsw.model;

public class Space {
    private boolean occupied;
    Level height;

    public boolean isDome(){
        if(height.equals(Level.DOME) && occupied == true){
            return true;
        }
        return false;
    }

    public void setOccupied(){
        occupied = true;
    }

    public void setUnoccupied(){
        occupied = false;
    }

    public boolean getOccupied(){
        return occupied;
    }

    public Level getHeight(){
        return height;
    }

    // TODO: eccezione se viene invocata su Space con cupola o player
    public void levelUp(){
        if(this.height != Level.DOME) {
            height = Level.values()[height.ordinal() + 1];
        }
    }
}
