package it.polimi.ingsw.controller;

import it.polimi.ingsw.listeners.ChallengerViewEventListener;
import it.polimi.ingsw.listeners.EventSource;
import it.polimi.ingsw.listeners.PlayerViewEventListener;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.PlayerView;
import it.polimi.ingsw.view.RemotePlayerView;

import java.util.List;

public class RealController implements PlayerViewEventListener, ChallengerViewEventListener {
    private final GameModel model;
    private final Setup setup;

    public RealController(GameModel model) {
        this.model = model;
        setup = new Setup(this.model);
    }

    @Override
    public void onWorkerChosen(EventSource source, Coord workerPos) {
        String nickname = ((RemotePlayerView) source).getNickname();

        if (!isCurrentPlayer(nickname)) {
            throw new IllegalStateException("Player " + nickname + "tried to choose a " +
                    "worker not in his turn.");
        }

        model.setWorkerChoice(workerPos);
        model.nextStep();
    }

    @Override
    public void onMoveChosen(EventSource source, Coord moveChoice) {
        String nickname = ((RemotePlayerView) source).getNickname();

        if (!isCurrentPlayer(nickname)) {
            throw new IllegalStateException("Player " + nickname + "tried to move not in his turn.");
        }

        model.setMove(moveChoice);
        model.nextStep();
    }

    @Override
    public void onBuildChosen(EventSource source, Coord buildChoice, Level level) {
        String nickname = ((RemotePlayerView) source).getNickname();

        if (!isCurrentPlayer(nickname)) {
            throw new IllegalStateException("Player " + nickname + "tried to build not in his turn.");
        }

        model.setBuild(buildChoice, level);
        model.nextStep();
    }

    private /*helper*/ boolean isCurrentPlayer(String nickname) {
        Player currentPlayer = model.getCurrentPlayer();
        return currentPlayer.getNickname().equals(nickname);
    }

    public void skipAction(EventSource source) {
        String nickname = ((PlayerView) source).getNickname();

        if (!isCurrentPlayer(nickname)) {
            throw new IllegalStateException("Player " + nickname + "tried to skip action not in his turn.");
        }

        model.setEnd();
        model.nextStep();
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
