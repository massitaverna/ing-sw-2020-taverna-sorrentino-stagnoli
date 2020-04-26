package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.controller.IllegalWorkerChoiceException;
import it.polimi.ingsw.exceptions.model.WorkerNotFoundException;
import it.polimi.ingsw.listeners.ChallengerViewEventListener;
import it.polimi.ingsw.listeners.EventSource;
import it.polimi.ingsw.listeners.PlayerViewEventListener;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.GameModel;

import java.util.List;

public class RealController implements PlayerViewEventListener, ChallengerViewEventListener {
    private GameModel model;
    private final Setup setup;

    public RealController(GameModel model) {
        this.model = model;
        setup = new Setup(this.model);
    }




    @Override
    public void onWorkerChosen(EventSource source, Coord workerPos) throws IllegalWorkerChoiceException, WorkerNotFoundException {

    }

    @Override
    public void onMoveChosen(EventSource source, Coord moveChoice) {

    }

    @Override
    public void onBuildChosen(EventSource source, Coord buildChoice) {

    }


    // --------------------------------------------------------------------------------
    /*
    SETUP SECTION
        Here event handling is delegated to Setup class.
    */

    @Override
    public void onNicknameChosen(EventSource source, String nickname) {
        setup.onNicknameChosen(source, nickname);
    }


    @Override
    public void onNumberOfPlayersChosen(EventSource source, int num) {
        setup.onNumberOfPlayersChosen(source, num);
    }

    @Override
    public void onGodsChosen(EventSource source, List<String> gods) {
        setup.onGodsChosen(source, gods);
    }

    @Override
    public void onGodChosen(EventSource source, String god) {
        setup.onGodChosen(source, god);
    }

    @Override
    public void onStartPlayerChosen(EventSource source, String startPlayerNickname) {
        setup.onStartPlayerChosen(source, startPlayerNickname);
    }

    @Override
    public void onWorkerInitialization(EventSource source, Coord choice) {
        setup.onWorkerInitialization(source, choice);
    }

}
