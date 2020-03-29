package it.polimi.ingsw.model;

import it.polimi.ingsw.view.listeners.Model.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class GameModel {

    private int numPlayers;
    private List<Player> queue;
    private List<God> godsList;
    private List<Color> colors;
    private Board board;
    private Player challenger;
    private Player currentPlayer;
    private PropertyChangeSupport mPcs;

    private List<ModelEventListener> modelListeners = new ArrayList<>();
    /*per sollevare un evento (esempio) :
        for(ModelEventListener l: modelListeners){
            l.onAllPlayersArrived();
        }*/


    public GameModel(int numPlayers){
        this.numPlayers = numPlayers;
        this.queue = new ArrayList<>();
        this.colors = new ArrayList<>();
        this.colors.add(Color.YELLOW);
        this.colors.add(Color.RED);
        this.colors.add(Color.BLUE);
        this.board = new Board();
        this.godsList = null;
        this.currentPlayer = null;
        this.mPcs = new PropertyChangeSupport(this);
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public Player getChallenger() {
        return challenger;
    }

    private boolean allPlayersArrived(){
        return this.queue.size() == this.numPlayers;
    }

    public void addNewPlayer(Player player){
        /*if (queue.isEmpty()) {
            challenger = player;
            currentPlayer = player;
        }*/
        queue.add(player);
        this.board.addWorker(player.getWorker(0));
        this.board.addWorker(player.getWorker(1));
        if(allPlayersArrived()){
            this.mPcs.firePropertyChange("allPlayersArrived", false, true);
        }
    }

    public List<Player> getQueue(){
        return null;
    }

    public void setGods(List<God> list){
        this.godsList = list;
    }

    public void setPlayerColor(Player p, Color c) throws Exception {
        //TODO: Check that player p is part of the game
        //If color has been choose by another player, throw exception
        if(!this.colors.contains(c))
            throw new Exception();
        p.setWorkerColor(c);
        this.colors.remove(c);
        mPcs.firePropertyChange("colors", null, this.colors);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void assignGodToPlayer(Player p, God g) throws IllegalArgumentException {
        //TODO: Check that player p is part of the game
        //If god has been choose by another player, throw exception
        if(!this.godsList.contains(g))
            throw new IllegalArgumentException("Chosen god is not available in this game.");
        p.setGod(g);
        this.godsList.remove(g); //  Rimuovo Player p dalla godsList???
    }

    public void setStartPlayer(Player startPlayer){
        //TODO: Check that player startPlyaer is part of the game

        boolean ordered = false;
        if (startPlayer.equals(queue.get(0))) ordered = true;
        while (!ordered) {
            Player moved = queue.remove(0);
            queue.add(moved);
            if (startPlayer.equals(queue.get(0))) ordered = true;
        }

        currentPlayer = startPlayer;
    }

    /*public Worker setPlayerWorkerChose(Player p, Worker w) {
        return null;
    }*/

    public void setPlayerMoveChose(Worker w, Coord m){
        /*this.board.workerMove(w, m);*/
        mPcs.firePropertyChange("board", null, board);
    }

    public void setPlayerBuildChose(Worker w, Coord b){
        /*this.board.workerBuild(w, b);*/
        mPcs.firePropertyChange("board", null, board);
    }

    public void setWin(Player p){
        //TODO: Check that player p is part of the game
        p.win();
        //TODO: End of the Game
    }

    public void nextPlayer(){
        //TODO: Check the game is ready
        this.queue.remove(currentPlayer);
        this.queue.add(currentPlayer);
        currentPlayer = this.queue.get(0);
        mPcs.firePropertyChange("turn", null, currentPlayer);
    }

    public Board getBoard(){
        return this.board;
    }

    //interrogazioni dalla gui
    public void getBoardView(){

    }

    public ArrayList<Worker> getAllWorkers(){
        ArrayList<Worker> workers = new ArrayList<>();
        for(Player p: this.queue){
            workers.add(p.getWorker(0));
            workers.add(p.getWorker(1));
        }
        return workers;
    }

    public void getOtherPlayersInfo(){

    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener){
        mPcs.addPropertyChangeListener(property, listener);
    }

    public int getQueueState(){
        return queue.size();
    }

    public Color[] getViableColorsToString(){
        return (Color[])this.colors.toArray();
    }
}
