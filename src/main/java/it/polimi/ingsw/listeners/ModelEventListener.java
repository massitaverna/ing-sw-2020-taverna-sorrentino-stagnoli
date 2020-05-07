package it.polimi.ingsw.listeners;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.model.Worker;

import java.util.List;
import java.util.Map;

public interface ModelEventListener extends Listener {
//    public void onAllPlayersArrived();
    public void onBoardChanged(Board board);
    public void onGameReady();
    public void onGodsChosen(List<String> gods);
    public void onPlayerAdded(String nickname, int numCurr, int numTot);
    public void onMessage(String message);

    public void onGodSelection(List<String> gods);
    public void onGodsSelection(List<String> gods, int numPlayers);
    public void onStartPlayerSelection(List<String> players);
    public void onMyInitialization(List<Coord> freeSpaces);

    public void onMyTurn(List<Coord> selectableWorkers);
    public void onMyAction(List<Coord> movableSpaces, Map<Level, List<Coord>> buildableSpaces, boolean canEndTurn);
    public void onWin(String winner);

    public String getNickname();
}
