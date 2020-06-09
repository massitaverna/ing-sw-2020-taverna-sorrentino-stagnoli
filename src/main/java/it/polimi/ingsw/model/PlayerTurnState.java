package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.ModelEventListener;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerTurnState extends ModelState {

    public PlayerTurnState(GameModel model) {
        super(model);
    }

    @Override
    public void nextStep() {
        if (model.hasNewTurnBegun()) {
            Player currPlayer = model.getCurrentPlayer();
            String nickname = currPlayer.getNickname();
            List<Coord> selectableWorkers = model.getSelectableWorkers();

            // Caught by tests
            if (model.getAllListeners().size() == 0) {
                throw new RuntimeException("No listeners found.");
            }

            if (!selectableWorkers.isEmpty()) {
                model.getAllListeners().forEach(l -> l.onMyTurn(nickname, selectableWorkers));
            } else {
                model.removeCurrentPlayer();
            }
        } else {
            model.nextAction();
        }
    }
}
