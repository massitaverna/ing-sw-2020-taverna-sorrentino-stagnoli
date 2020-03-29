package it.polimi.ingsw.listeners.View;

import java.util.List;

public interface ViewEventListener {
    public void onPlayerChoseBuild(String position);
    public void onColorChosen(String color);
    public void onPlayerChoseMove(String position);
    public void onNicknameChosen(String nick);
    public void onWorkerToMoveChosen(String workerID);
    public void onFirstWorkerPositioned(String position);
    public void onSecondWorkerPositioned(String position);
    public void onGodsChosen(List<String > gods);
    public void onGodChosen(String god);
    public void onStartingPlayerChosen(String playerNick);
    public void onNumberOfPlayerChosen(int num);
}
