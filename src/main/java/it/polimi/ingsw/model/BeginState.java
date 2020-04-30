package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.ModelEventListener;

public class BeginState extends ModelState {

    public BeginState(GameModel model) {
        super(model);
    }

    @Override
    public void nextStep() {
        model.initRequestHandlers();
        model.getPlayersNicknames().stream()
                .map(p -> model.getListenerByNickname(p))
                .forEach(ModelEventListener::onGameReady);
        model.changeState(new PlayerTurnState(model));
        model.nextStep();
    }
}
