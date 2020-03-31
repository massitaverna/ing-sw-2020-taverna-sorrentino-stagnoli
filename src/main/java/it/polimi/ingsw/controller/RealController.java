package it.polimi.ingsw.controller;

import it.polimi.ingsw.listeners.ChallengerViewEventListener;
import it.polimi.ingsw.listeners.EventSource;
import it.polimi.ingsw.listeners.ViewEventListener;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.ChallengerView;

import java.util.List;

public class RealController implements ViewEventListener, ChallengerViewEventListener {
    private GameModel model;
    private Setup setup;

    public RealController(GameModel model) {
        this.model = model;
        setup = new Setup(this.model);
    }

    @Override
    public void onPlayerChoseBuild(EventSource source, Coord position) {

    }

    @Override
    public void onPlayerChoseMove(EventSource source, Coord position) {

    }

    @Override
    public void onWorkerToMoveChosen(EventSource source, String workerID) {

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
    public void onColorChosen(EventSource source, Color color) {
        setup.onColorChosen(source, color);
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
    public void onWorkerInitialized(EventSource source, int whichWorker, int x, int y) {
        setup.onWorkerInitialized(source, whichWorker, x, y);
    }

}
