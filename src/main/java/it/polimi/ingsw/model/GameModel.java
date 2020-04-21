package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.model.WorkerNotFoundException;
import it.polimi.ingsw.listeners.EventSource;
import it.polimi.ingsw.listeners.Listener;
import it.polimi.ingsw.listeners.ModelEventListener;
import it.polimi.ingsw.model.god.God;

import java.util.ArrayList;
import java.util.List;

public class GameModel implements EventSource {

    private int numPlayers;
    private List<Player> queue;
    private List<God> godsList;
    private List<Color> colors;
    private Board board;
    private Player currentPlayer;
    private Worker currentWorker;
    private Coord currentMoveChose;
    private Coord currentBuildChose;

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
        this.currentWorker = null;
        this.currentMoveChose = null;
        this.currentBuildChose = null;
    }

    //SETUP FUNCTIONS//

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public boolean allPlayersArrived(){
        return this.queue.size() == this.numPlayers;
    }

    public void addNewPlayer(Player player){

        if (queue.size() == 0) {
            currentPlayer = player; // Set Challenger as currentPlayer
        }
        queue.add(player);
        this.board.addWorker(player.getWorker(0));
        this.board.addWorker(player.getWorker(1));

        /*if(allPlayersArrived()) {
            for (ModelEventListener listener : modelListeners) {
                listener.onAllPlayersArrived();
            }
        }*/
    }

    public void setGods(List<God> list){
        this.godsList = list;
    }

    public List<God> getGods() {
        return godsList;
    }

    public void setPlayerColor(Player p, Color c) throws IllegalArgumentException {
        //TODO: Check that player p is part of the game
        //If color has been choose by another player, throw exception
        if(!this.colors.contains(c))
            throw new IllegalArgumentException("Chosen color is not available any longer.");

        p.setWorkerColor(c);
        this.colors.remove(c);
    }

    public void assignGodToPlayer(Player p, God g) throws IllegalArgumentException {
        //TODO: Check that player p is part of the game
        //If god has been choose by another player, throw exception
        if(!this.godsList.contains(g))
            throw new IllegalArgumentException("Chosen god has been previously chosen by another player " +
                    "or has never been selected by Challenger.");

        p.setGod(g);
        this.godsList.remove(g);
    }

    public void setStartPlayer(Player startPlayer) throws IllegalArgumentException {

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

    public void initializeWorker(Worker w, Coord c) {
        board.initializeWorker(w, c);
    }

    //GAME FUNCTIONS//


    public Player getPlayerByNickname(String nick) throws IllegalArgumentException {

        Player res = queue.stream().filter(p -> p.getNickname().equals(nick)).findFirst().orElse(null);

        if (res != null) {
            return res;
        } else {
            throw new IllegalArgumentException("There is no player with the provided name.");
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setWorkerChoice(Coord workerPos) throws WorkerNotFoundException {
        Worker selected = this.board.getWorkerByPosition(workerPos);
        //TODO: Check that in workerPos there is a worker that belongs to currentPlayer
        this.currentWorker = selected;
    }

    public void setMove(Coord m){
        this.currentMoveChose = m;
    }

    public void setBuild(Coord b){
        this.currentBuildChose = b;
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

    //INTERROGAZIONI DALLE VIEW
    public Board getBoard(){
        return this.board;
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

    public List<Color> getAvailableColors(){
        return this.colors;
    }

    public List<God> getAvailableGods(){
        return this.godsList;
    }

    public List<String> requestPlayersNicknames() {
        List<String> res = new ArrayList<>();
        for(Player p: this.queue){
            res.add(p.getNickname());
        }
        return res;
    }

    @Override
    public void addListener(Listener listener) {
        if (!(listener instanceof ModelEventListener)) {
            throw new IllegalArgumentException("Tried to register a non-ModelEventListener to Model");
        }
        modelListeners.add((ModelEventListener) listener);
    }

    private ModelEventListener getListenerByNickname(String nickname) {
        return modelListeners.stream()
                .filter(listener -> listener.getNickname().equals(nickname)).
                findFirst().orElse(null);
    }
}
