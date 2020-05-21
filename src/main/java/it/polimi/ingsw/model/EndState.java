package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.ModelEventListener;

public class EndState extends ModelState {

    public EndState(GameModel model) {
        super(model);
    }

    @Override
    public void nextStep() {
        String winner = model.getCurrentPlayer().getNickname();
        model.getPlayersNicknames().stream()
                .map(nickname -> model.getListenerByNickname(nickname))
                .forEach(l -> l.onMessage(winner + " has won !!"));
        model.getPlayersNicknames().stream()
                .map(nickname -> model.getListenerByNickname(nickname))
                .forEach(ModelEventListener::onEnd);
    }
}
