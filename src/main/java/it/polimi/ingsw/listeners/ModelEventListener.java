package it.polimi.ingsw.listeners;

import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.Worker;

import java.util.List;

public interface ModelEventListener extends Listener {
    public void onAllPlayersArrived();
    public void onBoardChanged();
    public void onGameReady();
    public void onGodsChosen();
    public void onPlayerAdded(String nickname);
    public void onMyTurn(List<Worker> selectableWorkers);
    public void onMyAction(List<Coord> movableSpaces, List<Coord> buildableSpaces);

/*  public String getNickname();*/

}
