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

            //TODO: Non vanno passati tutti i worker, ma solo quelli validi (vedi simulate())
            List<Worker> allWorkers = currPlayer.getWorkersList();
            List<Coord> selectableWorkers = allWorkers.stream().map(Worker::getPosition)
                    .collect(Collectors.toList());
            listener.onMyTurn(selectableWorkers);
        } else {
            model.nextAction();
        }
    }
}
