package it.polimi.ingsw.model;

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

/*    //messi per poter eseguire i test (in realtà non sono necessari, il controller controlla che le mosse siano lecite,
    // oppure i client non possono fare mosse illecite perchè gli viene detto cosa possono fare e non possono fare altrimenti)
    //giusto????
    private Coord moveChose;
    private Coord buildChose;
    private Worker workerChose;*/

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
    }

    //SETUP FUNCTIONS//

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    private boolean allPlayersArrived(){
        return this.queue.size() == this.numPlayers;
    }

    public void addNewPlayer(Player player){

        queue.add(player);
        this.board.addWorker(player.getWorker(0));
        this.board.addWorker(player.getWorker(1));

        if(allPlayersArrived()){
            for (ModelEventListener listener: modelListeners) {
                listener.onAllPlayersArrived();
            }
        }
    }

    public void setGods(List<God> list){
        this.godsList = list;
    }

    public List<God> getGods() {
        return godsList;
    }

    public void setPlayerColor(Player p, Color c) throws IllegalStateException {
        //TODO: Check that player p is part of the game
        //If color has been choose by another player, throw exception
        if(!this.colors.contains(c))
            throw new IllegalStateException("Chosen color is not available any longer.");
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


    /*public Worker setPlayerWorkerChose(Player p, Worker w) {
        return null;
    }*/

    public Player getPlayerByNickname(String nick) throws Exception{
        Player res = null;
        for(Player p: this.queue){
            if(p.getNickname().compareTo(nick) == 0){
                res = p;
            }
        }
        if (res != null){
            return res;
        }else{
            throw new Exception();
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setPlayerWorkerChose(Coord workerPos) throws Exception{
        Worker selected = this.board.getWorkerByPosition(workerPos);
        //TODO: Check that in workerPos there is a worker that belongs to currentPlayer
        this.currentWorker = selected;
    }

    public void setPlayerMoveChose(Coord m) throws Exception{
        this.board.workerMove(currentWorker, m);
    }

    public void setPlayerBuildChose(Coord b) throws Exception{
        this.board.workerBuild(currentWorker, b);
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

    public List<Color> getAvailableColors(){
        return colors;
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
}
