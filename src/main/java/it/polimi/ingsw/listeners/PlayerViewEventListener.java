//mod
package it.polimi.ingsw.listeners;


import it.polimi.ingsw.exceptions.controller.IllegalPlayerException;
import it.polimi.ingsw.exceptions.controller.IllegalWorkerChoiceException;
import it.polimi.ingsw.exceptions.model.WorkerNotFoundException;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.god.God;

public interface PlayerViewEventListener {
   /* public void onBuildChosen(Coord position);
    public void onColorChosen(Color color);
    public void onMoveChosen(Coord position);
    public void onNicknameChosen(String nickname);
    public void onWorkerChosen(String workerID);
    public void onWorkerInitialized(int x, int y);
    public void onGodChosen(String god);*/


    public void onMyGodChoice(String playerNickname, God god) throws IllegalPlayerException;
    public void onMyInitializationChoice(String playerNickname, Coord choice) throws WorkerNotFoundException, IllegalWorkerChoiceException;

    public void onWorkerChosen(String playerNickname, Coord workerPos) throws IllegalWorkerChoiceException, WorkerNotFoundException;
    public void onMoveChosen(String playerNickname, Coord moveChoice);
    public void onBuildChosen(String playerNickname, Coord buildChoice);
}
