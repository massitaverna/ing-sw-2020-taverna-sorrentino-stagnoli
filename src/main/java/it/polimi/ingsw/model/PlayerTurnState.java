package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.ModelEventListener;

public class PlayerTurnState extends ModelState {

    public PlayerTurnState(GameModel model) {
        super(model);
    }

    @Override
    public void nextStep() {
        if (model.hasNewTurnBegun()) {
            String currPlayer = model.getCurrentPlayer().getNickname();
            ModelEventListener listener = model.getListenerByNickname(currPlayer);
            listener.onMyTurn();
        }
        model.nextAction();
    }
}
