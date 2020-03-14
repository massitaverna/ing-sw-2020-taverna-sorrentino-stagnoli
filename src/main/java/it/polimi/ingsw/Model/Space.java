package it.polimi.ingsw.Model;

public class Space {

    private boolean occupied;
    private boolean dome;
    private Level height;

    public Space(){
        this.occupied=false;
        this.height=Level.GROUND;
        this.dome=false;
    }

    //ritorna true se occupata
    public boolean getOccupied(){
        return this.occupied;
    }

    //ritorna true se è ad altezza cupola
    public boolean getDome(){
        return this.height == Level.DOME;
    }

    //ritorna l'altitudine
    public Level getLevel(){
        return this.height;
    }

    //occupa lo spazio
    public void Occupy(){
        this.occupied=true;
    }

    //libera lo spazio
    public void Free(){
        this.occupied=false;
    }

    //costruisci su questo spazio
    public void Build() throws SpaceFullException {
        switch(this.height){
            case DOME:
                throw new SpaceFullException("CANNOT BUILD OVER DOMES");
            case GROUND:
                this.height = Level.L1;
                break;
            case L1:
                this.height = Level.L2;
                break;
            case L2:
                this.height = Level.L3;
                break;
            case L3:
                this.height = Level.DOME;
                break;
        }
    }

}
