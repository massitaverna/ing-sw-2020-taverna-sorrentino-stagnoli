package it.polimi.ingsw.model;

public class Space {
    private boolean occupied;
    Level height;

    public boolean isDome(){
        if(height.equals(Level.DOME)){
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

    public boolean isOccupied(){
        return occupied;
    }

    public Level getHeight(){
        return height;
    }

    // TODO: eccezione se viene invocata su Space con cupola o player
    public void levelUp() throws Exception{
        if(this.height != Level.DOME) {
            height = Level.values()[height.ordinal() + 1];
        }
        else
            throw new Exception();
    }
}
