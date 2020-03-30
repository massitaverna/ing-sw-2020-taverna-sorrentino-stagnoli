package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.Model.*;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class GameModel {

    private int numPlayers;
    private List<Player> queue;
    private List<God> godsList;
    private List<Color> colors;
    private Board board;
    private Player challenger;
    private Player currentPlayer;

    //messi per poter eseguire i test (in realtà non sono necessari, il controller controlla che le mosse siano lecite,
    // oppure i client non possono fare mosse illecite perchè gli viene detto cosa possono fare e non possono fare altrimenti)
    //giusto????
    private Coord moveChose;
    private Coord buildChose;
    private Worker workerChose;

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
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
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
            for (ModelEventListener listener: modelListeners) {
                listener.onAllPlayersArrived();
            }
        }
    }

    public Player getPlayerByNickname(String nick){
        Player res = null;
        for(Player p: this.queue){
            if(p.getNickname().compareTo(nick) == 0){
                res = p;
            }
        }
        return res;
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

    public void setStartPlayer(Player startPlayer) throws IllegalArgumentException{

        if(!queue.contains(startPlayer))
            throw new IllegalArgumentException("Chosen player is not in the game.");

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
    }

    public void setPlayerBuildChose(Worker w, Coord b){
        /*this.board.workerBuild(w, b);*/
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
    }

    public Board getBoard(){
        return this.board;
    }

    //INTERROGAZIONI DALLE VIEW
    public void getBoardView(){
        // using a CLI
        board.toString();
    }

    public ArrayList<Worker> getAllWorkers(){
        ArrayList<Worker> workers = new ArrayList<>();
        for(Player p: this.queue){
            workers.add(p.getWorker(0));
            workers.add(p.getWorker(1));
        }
        return workers;
    }

    public int getQueueState(){
        return queue.size();
    }

    public Color[] getViableColors(){
        return (Color[])this.colors.toArray();
    }

    public List<String> requestPlayersNicknames() {
        List<String> res = new ArrayList<>();
        for(Player p: this.queue){
            res.add(p.getNickname());
        }
        return res;
    }
}
