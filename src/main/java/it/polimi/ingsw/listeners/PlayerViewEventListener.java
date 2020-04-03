package it.polimi.ingsw.listeners;

import it.polimi.ingsw.listeners.Listener;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Coord;


public interface PlayerViewEventListener extends Listener {
    public void onPlayerChoseBuild(EventSource source, Coord position);
    public void onColorChosen(EventSource source, Color color);
    public void onPlayerChoseMove(EventSource source, Coord position);
    public void onNicknameChosen(EventSource source, String nickname);
    public void onWorkerToMoveChosen(EventSource source, String workerID);
    public void onWorkerInitialized(EventSource source, int x, int y);
    public void onGodChosen(EventSource source, String god);
}
