package it.polimi.ingsw.listeners;

import it.polimi.ingsw.listeners.Listener;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.Worker;

import java.util.List;

public interface ViewEventListener extends Listener {

    public void onNicknameChosen(String nick);
    public void onColorChosen(Color color);
    /*public void onWorkerToMoveChosen(String workerID);   ??? */
    public void onGodChosen(String god);

    public void onPlayerChoseWorker(Coord workerPos);
    public void onPlayerChoseMove(Coord position);
    public void onPlayerChoseBuild(Coord position);

}
