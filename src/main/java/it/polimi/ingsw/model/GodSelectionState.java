package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.ModelEventListener;

import java.util.ArrayList;
import java.util.List;

public class GodSelectionState extends ModelState {


    public GodSelectionState(GameModel model) {
        super(model);
    }

    @Override
    public void nextStep() {
        List<String> godsNames = new ArrayList<>();
        model.getAvailableGods().forEach(g -> godsNames.add(g.getName()));
        boolean areGodsChosen = model.getAvailableGods().size() <= model.getNumPlayers() ;

        if (!areGodsChosen) {
            /*String challenger = model.getCurrentPlayer().getNickname();
            ModelEventListener challengerListener = model.getListenerByNickname(challenger);
            challengerListener.onGodsSelection(godsNames, model.getNumPlayers());*/

            //all players should receive onGodsSelection:
            for(String nickname :model.getPlayersNicknames()){
                model.getListenerByNickname(nickname).onGodsSelection(godsNames, model.getNumPlayers());
            }

            return;
        }

        String currPlayer = model.getCurrentPlayer().getNickname();
        ModelEventListener currPlayerListener = model.getListenerByNickname(currPlayer);
        currPlayerListener.onGodSelection(godsNames);
    }
}
