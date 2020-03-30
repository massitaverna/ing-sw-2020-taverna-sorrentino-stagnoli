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
    public void onGodChosen(String god);
}
