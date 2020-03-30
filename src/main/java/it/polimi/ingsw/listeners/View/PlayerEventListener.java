package it.polimi.ingsw.listeners.View;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Coord;

import java.util.List;

public interface PlayerEventListener {
    public void onPlayerChoseBuild(Coord position);
    public void onColorChosen(Color color);
    public void onPlayerChoseMove(Coord position);
    public void onNicknameChosen(String nick);
    public void onWorkerToMoveChosen(String workerID);
    public void onFirstWorkerPositioned(String position);
    public void onSecondWorkerPositioned(String position);
    public void onGodChosen(String god);
    public void onGodsChosen(List<String> gods);
    public void onStartingPlayerChosen(String playerNick);
    public void onNumberOfPlayerChosen(int num);
}
