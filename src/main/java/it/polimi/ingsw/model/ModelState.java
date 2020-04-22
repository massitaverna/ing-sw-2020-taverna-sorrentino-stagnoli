package it.polimi.ingsw.model;

import it.polimi.ingsw.model.GameModel;

public abstract class ModelState {
    GameModel model;

    public ModelState(GameModel model) {
        this.model = model;
    }

    public abstract void nextStep();
}
