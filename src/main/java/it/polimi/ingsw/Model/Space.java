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

    public boolean getOccupied(){
        return this.occupied;
    }

    public boolean getDome(){
        return this.height == Level.DOME;
    }

    public Level getLevel(){
        return this.height;
    }

    public void Occupy(){
        this.occupied=true;
    }

    public void Free(){
        this.occupied=false;
    }

    public void Build() throws SpaceFullException {
        switch(this.height){
            case DOME:
                throw new SpaceFullException("CANNOT BUILD OVER DOMES");
            case GROUND:
                this.height = Level.L1;
            case L1:
                this.height = Level.L2;
            case L2:
                this.height = Level.L3;
            case L3:
                this.height = Level.DOME;
        }
    }

}
