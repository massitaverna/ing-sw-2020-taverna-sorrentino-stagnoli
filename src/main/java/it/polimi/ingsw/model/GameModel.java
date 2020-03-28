package it.polimi.ingsw.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class GameModel extends Observable {

    private int numPlayers;
    private List<Player> queue;
    private List<God> godsList;
    private Board board;
    private Player challenger;
    private Player currentPlayer;
    private PropertyChangeSupport mPcs =
            new PropertyChangeSupport(this);


    // Controller must build an empty GameModel --> default constructor used
    /*public GameModel(int numPlayers, Player challenger){
        this.numPlayers = numPlayers;
        this.queue = new ArrayList<Player>();
        this.queue.add(challenger);
        this.godsList = null;
    }*/

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public Player getChallenger() {
        return challenger;
    }

    public boolean isReady(){
        return this.queue.size() == this.numPlayers;
    }

    public void addNewPlayer(Player player) {
        if (queue.isEmpty()) {
            challenger = player;
            currentPlayer = player;
        }
        queue.add(player);
        setChanged();
        notifyObservers(challenger.getNickname());
    }

    public List<Player> getQueue(){
        return null;
    }

    public void setGods(List<God> list){
        this.godsList = list;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void assignGodToPlayer(Player p, God g){
        //TODO: Check that players p is part of the game
        p.setGod(g);
    }

    public void setStartPlayer(Player startPlayer){
        //TODO: Check that players p is part of the game

        boolean ordered = false;

        if (startPlayer.equals(queue.get(0))) ordered = true;
        while (!ordered) {
            Player moved = queue.remove(0);
            queue.add(moved);
            if (startPlayer.equals(queue.get(0))) ordered = true;
        }

        currentPlayer = startPlayer;
        setChanged();
    }

    public Worker setPlayerWorkerChose(Player p, Worker w) {
        //TODO: Check that players p is part of the game
        return null;
    }

    public void setPlayerMoveChose(Player p, Worker w, Coord m){
        //TODO: Check that players p is part of the game

        mPcs.firePropertyChange("board", null, board);
    }

    public void setPlayerBuildChose(Player p, Worker w, Coord b){
        //TODO: Check that players p is part of the game

        mPcs.firePropertyChange("board", null, board);
    }

    public void setWin(Player p){
        //TODO: Check that players p is part of the game
        p.win();
        //TODO: End of the Game
    }

    public void nextPlayer(){
        //TODO: Check the game is ready
        this.queue.remove(currentPlayer);
        this.queue.add(currentPlayer);
        currentPlayer = this.queue.get(0);
        mPcs.firePropertyChange("currentPlayer", null, currentPlayer);
    }

    public Board getBoard(){
        return this.board;
    }

    //interrogazioni dalla gui
    public void getBoardView(){

    }

    public ArrayList<Worker> getAllWorkers(){
        ArrayList<Worker> workers = new ArrayList<>();
        //TODO: implement later
        return workers;
    }

    public void getOtherPlayersInfo(){

    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        mPcs.addPropertyChangeListener(listener);
    }

    public int getQueueState(){
        return queue.size();
    }

    public String getViableColorsToString(){
        String output = "(RED, BLUE, YELLOW)";

        //TODO: ciclare la coda per vedere quali colori sono stati presi e modificare output

        return output;
    }
}
