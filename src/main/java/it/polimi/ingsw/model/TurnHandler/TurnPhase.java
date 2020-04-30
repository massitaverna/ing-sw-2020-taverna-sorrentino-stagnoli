package it.polimi.ingsw.model.TurnHandler;

public enum TurnPhase {
    MOVE, BUILD, OPTIONALMOVE, OPTIONALBUILD;

    public boolean isMove(){
        return (this == MOVE || this == OPTIONALMOVE);
    }

    public boolean isBuild(){
        return (this == BUILD || this == OPTIONALBUILD);
    }

    public boolean isOptional(){
        return (this == OPTIONALMOVE || this == OPTIONALBUILD);
    }

    public boolean ofSameType(TurnPhase other) { return (this.isMove() && other.isMove()) || this.isBuild() && other.isBuild(); }
}
