package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.GameModel;

public abstract class ModelState {
    private GameModel model;

    public abstract void nextStep();
}
