package it.polimi.ingsw.listeners;

import java.util.List;

public interface ChallengerViewEventListener {
    public void onGodsChosen(List<String> gods);
    public void onStartingPlayerChosen(String playerNick);
    public void onNumberOfPlayerChosen(int num);
}
