package it.polimi.ingsw.model;

import java.util.Observable;

public class Model extends Observable {

    Game game;
    Board board;
    Player challanger;

    public Model(int numPlayers){
        this.game = new Game(numPlayers);
        this.board = new Board();
    }

    public void AddPlayer(Player p){
        this.game.addPlayer(p);
    }

    public void SetChallanger(Player p){
        this.challanger = p;
    }

    public void SetChosenGods(God[] list){
        game.setChosenGods(list);
    }

    public void SetGodChoose(Player p, God g){
        p.setGod(g);
    }

    public void SetStartingPlayerChoose(Player p){
        this.game.createGameQueue();
    }

    public Worker SetPlayerWorkerChose(Player p, Worker w){

    }

    public void SetPlayerMoveChose(Player p, Worker w, Coord m){

    }

    public void SetPlayerBuildChose(Player p, Worker w, Coord b){

    }

    public void SetWin(Player p){
        p.win();
    }

    public void SetLose(Player p){
        p.lose();
    }

    public void GetPlayersList(){
        return game.getAllPlayers();
    }

    public Player GetNextPlayer(){
        return this.game.NextPlayer();
    }

    public Board GetGameBoard(){
        return this.board;
    }

    public bool isGameReady(){
        return  this.game.isReady();
    }

    //interrogazioni dalla gui
    public void GetBoardView(){

    }

    public void GetAllWorkersPositions(){

    }

    public void GetOtherPlayersInfo(){

    }





}
