package it.polimi.ingsw.Model;

public enum Level {
    GROUND, L1, L2, L3, DOME;

    public boolean isDome(){
        return this==DOME;
    }

}
