package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.controller.IllegalPlayerException;
import it.polimi.ingsw.exceptions.controller.IllegalWorkerChoiceException;
import it.polimi.ingsw.exceptions.controller.InvalidPlayerException;
import it.polimi.ingsw.exceptions.model.WorkerNotFoundException;
import it.polimi.ingsw.listeners.ChallengerViewEventListener;
import it.polimi.ingsw.listeners.PlayerViewEventListener;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.God;

import java.util.List;

public class Controller implements PlayerViewEventListener, ChallengerViewEventListener {
    private GameModel model;
    /*private Setup setup;*/

    public Controller(GameModel model) {
        this.model = model;
        /*setup = new Setup(this.model);*/
    }

        /*@Override
    public void onPlayerChoseBuild(EventSource source, Coord position) {

    }

    @Override
    public void onPlayerChoseMove(EventSource source, Coord position) {

    }

    @Override
    public void onWorkerChosen(EventSource source, String workerID) {

    }

    // --------------------------------------------------------------------------------
    *//*
    SETUP SECTION
        Here event handling is delegated to Setup class.
    *//*

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
    public void onWorkerInitialized(EventSource source, int x, int y) {
        setup.onWorkerInitialized(source, x, y);
    }
*/

    //Controlla che il turno sia del player con questo nickname

    private boolean isCurrentPlayer(String nickname){
        Player currentPlayer = this.model.getCurrentPlayer();

        if(!currentPlayer.getNickname().equals(nickname)){
            //non è il suo turno
            return false;
        }
        return true;
    }

    @Override
    public void onGodsChosen(List<God> gods) {
        this.model.setGods(gods);
    }

    @Override
    public void onStartPlayerChosen(String nickname) throws InvalidPlayerException {
        if(!this.model.requestPlayersNicknames().contains(nickname)){
            throw new InvalidPlayerException("Player " + nickname + " doesn't exist.");
        }

        this.model.setStartPlayer(nickname);
    }

    @Override
    public void onMyGodChoice(String playerNickname, God god) throws IllegalPlayerException, IllegalArgumentException {
        //Controlla che sia il player corrente
        if(!isCurrentPlayer(playerNickname)){
            throw new IllegalPlayerException("Player took a choice but it's not the current player.");
        }

        Player currentPlayer = this.model.getCurrentPlayer();

        //assegna il god al player, controlla che il god sia nella lista
        this.model.assignGodToPlayer(currentPlayer, god);
    }

    @Override
    public void onMyInitializationChoice(String playerNickname, Coord choice) throws WorkerNotFoundException, IllegalWorkerChoiceException {
        //Controlla che sia il player corrente
        if(isCurrentPlayer(playerNickname)){
            Player currentPlayer = this.model.getCurrentPlayer();

            //seleziona il worker dalla board, controlla che un worker esista in quella posizione
            Worker chosenWorker = this.model.getBoard().getWorkerByPosition(choice);

            //controlla che il worker appartenga al giocatore corrente
            if(!chosenWorker.getPlayerNickname().equals(playerNickname)){
                throw new IllegalWorkerChoiceException("The worker in position " + choice.x + " " + choice. y +
                        " doesn't belong to the current player.");
            }

            this.model.initializeWorker(chosenWorker, choice);
        }
    }

    @Override
    public void onWorkerChosen(String playerNickname, Coord workerPos) throws WorkerNotFoundException, IllegalWorkerChoiceException {
        //Controlla che sia il player corrente
        if(isCurrentPlayer(playerNickname)) {
            Player currentPlayer = this.model.getCurrentPlayer();

            //seleziona il worker dalla board, controlla che un worker esista in quella posizione
            Worker chosenWorker = this.model.getBoard().getWorkerByPosition(workerPos);

            //controlla che il worker appartenga al giocatore corrente
            if(!chosenWorker.getPlayerNickname().equals(playerNickname)){
                throw new IllegalWorkerChoiceException("The worker in position " + workerPos.x + " " + workerPos. y +
                        " doesn't belong to the current player.");
            }

            this.model.setWorkerChoice(workerPos);
        }
    }

    @Override
    public void onMoveChosen(String playerNickname, Coord moveChoice) {
        //Controlla che sia il player corrente
        if(isCurrentPlayer(playerNickname)) {
            Player currentPlayer = this.model.getCurrentPlayer();

            this.model.setMove(moveChoice);
        }
    }

    @Override
    public void onBuildChosen(String playerNickname, Coord buildChoice) {
        //Controlla che sia il player corrente
        if(isCurrentPlayer(playerNickname)) {
            Player currentPlayer = this.model.getCurrentPlayer();

            this.model.setMove(buildChoice);
        }
    }
}
