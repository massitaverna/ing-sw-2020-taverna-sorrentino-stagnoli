package it.polimi.ingsw.model;

public class BeginState extends ModelState {

    public BeginState(GameModel model) {
        super(model);
    }

    @Override
    public void nextStep() {
        model.initRequestHandlers();
    }
}
