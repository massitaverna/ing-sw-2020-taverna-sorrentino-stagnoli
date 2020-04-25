package it.polimi.ingsw.model;

public class PlayerTurnState extends ModelState {

    public PlayerTurnState(GameModel model) {
        super(model);
    }

    @Override
    public void nextStep() {
        model.nextAction();
    }
}
