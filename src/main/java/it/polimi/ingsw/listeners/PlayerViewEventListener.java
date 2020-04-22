package it.polimi.ingsw.listeners;


import it.polimi.ingsw.exceptions.controller.IllegalPlayerException;
import it.polimi.ingsw.exceptions.controller.IllegalWorkerChoiceException;
import it.polimi.ingsw.exceptions.model.WorkerNotFoundException;
import it.polimi.ingsw.model.Coord;


public interface PlayerViewEventListener extends Listener {
    /*
    public void onBuildChosen(EventSource source, Coord position);
    public void onColorChosen(EventSource source, Color color);
    public void onMoveChosen(EventSource source, Coord position);
    public void onWorkerChosen(EventSource source, String workerID);
    public void onWorkerInitialized(EventSource source, int x, int y);
    public void onGodChosen(EventSource source, String god);
     */

    // Da riscrivere con gli EventSource, vedi sopra
    public void onMyGodChoice(EventSource source, String god) throws IllegalPlayerException;
    public void onWorkerInitialization(EventSource source, Coord choice) throws WorkerNotFoundException, IllegalWorkerChoiceException;
    public void onNicknameChosen(EventSource source, String nickname);
    public void onWorkerChosen(EventSource source, Coord workerPos) throws IllegalWorkerChoiceException, WorkerNotFoundException;
    public void onMoveChosen(EventSource source, Coord moveChoice);
    public void onBuildChosen(EventSource source, Coord buildChoice);
}
