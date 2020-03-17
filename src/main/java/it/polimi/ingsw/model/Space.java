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
}
