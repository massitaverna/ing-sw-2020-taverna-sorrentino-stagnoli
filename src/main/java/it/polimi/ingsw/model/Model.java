package it.polimi.ingsw.model;

import java.util.Observable;

public class Model extends Observable {

    Game game;
    Board board;
    Player challenger;

    public Model(int numPlayers){
        this.game = new Game(numPlayers);
        this.board = new Board();
    }

    public void addPlayer(Player p){
        this.game.addPlayer(p);
    }

    public void setChallenger(Player p){
        this.challenger = p;
    }

    public void setChosenGods(God[] list){
        game.setChosenGods(list);
    }

    public void setGodChoose(Player p, God g){
        p.setGod(g);
    }

    public void setStartingPlayerChoose(Player p){
        this.game.createGameQueue();
    }

    public Worker setPlayerWorkerChose(Player p, Worker w){

    }

    public void setPlayerMoveChose(Player p, Worker w, Coord m){

    }

    public void setPlayerBuildChose(Player p, Worker w, Coord b){

    }

    public void setWin(Player p){
        p.win();
    }

    public void setLose(Player p){
        p.lose();
    }

    public void getPlayersList(){
        return game.getAllPlayers();
    }

    public Player getNextPlayer(){
        return this.game.nextPlayer();
    }

    public Board getGameBoard(){
        return this.board;
    }

    public boolean isGameReady(){
        return  this.game.isReady();
    }

    //interrogazioni dalla gui
    public void getBoardView(){

    }

    public void getAllWorkersPositions(){

    }

    public void getOtherPlayersInfo(){

    }





}
