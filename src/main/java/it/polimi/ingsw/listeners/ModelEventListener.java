package it.polimi.ingsw.listeners;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.Worker;

import java.util.List;

public interface ModelEventListener extends Listener {
//    public void onAllPlayersArrived();
    public void onBoardChanged(Board board);
    public void onGameReady();
    public void onGodsChosen(List<String> gods);
    public void onPlayerAdded(String nickname);

    public void onGodSelection(List<String> gods);
    public void onGodsSelection(List<String> gods, int numPlayers);
    public void onStartPlayerSelection(List<String> players);
    public void onMyInitialization(List<Coord> freeSpaces);

    public void onMyTurn(List<Worker> selectableWorkers);
    public void onMyAction(List<Coord> movableSpaces, List<Coord> buildableSpaces);
}
