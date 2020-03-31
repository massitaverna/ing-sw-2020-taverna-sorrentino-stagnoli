package it.polimi.ingsw.listeners.View;

import java.util.List;

public interface ChallengerEventListener {
    public void onGodsChosen(List<String> gods);
    public void onStartingPlayerChosen(String playerNick);
    public void onNumberOfPlayerChosen(int num);
}
