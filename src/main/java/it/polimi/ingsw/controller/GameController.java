package it.polimi.ingsw.controller;

import it.polimi.ingsw.listeners.View.ChallengerViewEventListener;
import it.polimi.ingsw.listeners.View.PlayerEventListener;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

//implementato da nico, solo una prova
public class GameController implements ChallengerViewEventListener, PlayerEventListener {

    private GameModel model;

    public GameController(GameModel model){
        this.model = model;
    }

    @Override
    public void onGodsChosen(List<String> gods) {
        List<God> godsList = new ArrayList<>();
        //TODO: convertire i god da stringa a God
        this.model.setGods(godsList);
    }

    @Override
    public void onStartingPlayerChosen(String playerNick) throws Exception {
        Player startPlayer = this.model.getPlayerByNickname(playerNick);
        if(startPlayer == null){
            throw new Exception("the choosen nickname is not part of the game");
        }
    }

    @Override
    public void onNumberOfPlayerChosen(int num) {
        this.model.setNumPlayers(num);
    }

    @Override
    public void onPlayerChoseBuild(String position) {

    }

    @Override
    public void onColorChosen(String color) {

    }

    @Override
    public void onPlayerChoseMove(String position) {

    }

    @Override
    public void onNicknameChosen(String nick) {

    }

    @Override
    public void onWorkerToMoveChosen(String workerID) {

    }

    @Override
    public void onFirstWorkerPositioned(String position) {

    }

    @Override
    public void onSecondWorkerPositioned(String position) {

    }

    @Override
    public void onGodChosen(String god) {

    }
}
