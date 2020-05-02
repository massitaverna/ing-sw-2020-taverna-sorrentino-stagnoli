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
            ModelEventListener listener = model.getListenerByNickname(nickname);
            List<Worker> selectableWorkers = model.getSelectableWorkers();
            List<Coord> selectableCoords = selectableWorkers.stream().map(Worker::getPosition)
                    .collect(Collectors.toList());
            listener.onMyTurn(selectableCoords);
        } else {
            model.nextAction();
        }
    }
}
