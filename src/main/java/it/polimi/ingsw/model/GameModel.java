package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class GameModel {

    private int numPlayers;
    private List<Player> queue;
    private List<God> godsList;
    private Board board;

    public GameModel(int numPlayers, Player challenger){
        this.numPlayers = numPlayers;
        this.queue = new ArrayList<Player>();
        this.queue.add(challenger);
        this.godsList = null;
    }

    public boolean isReady(){
        return this.queue.size() == this.numPlayers;
    }

    public void addPlayer(Player player){
        queue.add(player);
    }

    public List<Player> getQueue(){
        return null;
    }

    public void setChosenGods(List<God> list){
        this.godsList = list;
    }

    public void setGodChoose(Player p, God g){
        //TODO: Check that players p is part of the game
        p.setGod(g);
    }

    public void setStartingPlayerChoose(Player p){
        //TODO: Check that players p is part of the game
        this.queue.remove(p);
        this.queue.add(0, p);
    }

    public Worker setPlayerWorkerChose(Player p, Worker w){
        //TODO: Check that players p is part of the game

    }

    public void setPlayerMoveChose(Player p, Worker w, Coord m){
        //TODO: Check that players p is part of the game

    }

    public void setPlayerBuildChose(Player p, Worker w, Coord b){
        //TODO: Check that players p is part of the game

    }

    public void setWin(Player p){
        //TODO: Check that players p is part of the game
        p.win();
        //TODO: End of the Game
    }

    public Player nextPlayer(){
        //TODO: Check the game is ready
        Player p = this.queue.get(0);
        this.queue.remove(p);
        this.queue.add(p);
        return p;
    }

    public Board getBoard(){
        return this.board;
    }

    //interrogazioni dalla gui
    public void getBoardView(){

    }

    public void getAllWorkersPositions(){

    }

    public void getOtherPlayersInfo(){

    }

}
