package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.ModelEventListener;

import java.util.List;

public class WorkersInitState extends ModelState {

    private boolean startPlayerChosen = false;
    public WorkersInitState(GameModel model) {
        super(model);
    }

    @Override
    public void nextStep() {
        if (!startPlayerChosen) {
            startPlayerChosen = true;

            /*String challenger = model.getCurrentPlayer().getNickname();
            ModelEventListener challengerListener = model.getListenerByNickname(challenger);
            challengerListener.onStartPlayerSelection(model.getPlayersNicknames());*/

            //all players should receive onStartPlayerSelection:
            for(String nickname :model.getPlayersNicknames()){
                model.getListenerByNickname(nickname).onStartPlayerSelection(model.getPlayersNicknames());
            }

            return;
        }

        List<Coord> freeSpaces = model.getBoard().getUnoccupiedSpaces();
        String currPlayer = model.getCurrentPlayer().getNickname();
        ModelEventListener currPlayerListener = model.getListenerByNickname(currPlayer);
        currPlayerListener.onMyInitialization(freeSpaces);

    }
}
