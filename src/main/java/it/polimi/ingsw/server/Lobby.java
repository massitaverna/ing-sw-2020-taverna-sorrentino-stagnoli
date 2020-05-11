package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.RealController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.view.RemoteChallengerView;
import it.polimi.ingsw.view.RemotePlayerView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Lobby {

    /*private class PingChecker implements Runnable{
        @Override
        public void run() {

        }
    }*/

    private GameModel model;
    private RealController controller;

    private List<RemotePlayerView> playersViews;
    private RemotePlayerView challengerView;

    private ExecutorService executor = Executors.newCachedThreadPool();
    //private ExecutorService pingExecutor = Executors.newScheduledThreadPool(1);

    public Lobby(){
        this.model = new GameModel();
        this.controller = new RealController(this.model);
        this.playersViews = new ArrayList<>();
    }

    public synchronized boolean isFull(){
        return this.playersViews.size() == this.model.getNumPlayers();
    }

    public synchronized List<String> getPlayersNicknames(){
        List<String> result = new ArrayList<>();
        for(RemotePlayerView v: this.playersViews){
            result.add(v.getNickname());
        }
        return result;
    }

    public synchronized boolean addPlayer(String nickname, Socket socket, ObjectOutputStream o, ObjectInputStream i){

        //if name is null or is already present, return false
        if(nickname == null || nickname.equals("") || this.playersViews.stream().anyMatch(x -> x.getNickname().equals(nickname)) )
            return false;

        if(socket.isClosed())
            return false;

        RemotePlayerView playerView = new RemotePlayerView(nickname, new Connection(socket, this, o, i));
        //start a separate thread waiting for client messages
        this.executor.submit(playerView.getClientConnection());
        //add to the list of players
        this.playersViews.add(playerView);
        //pass the controller to make the view to add it as listener
        playerView.addListener(controller);
        //the player view is a listener of the model
        this.model.addListener(playerView);

        //if it is the first player coming, he is the challenger
        if(this.playersViews.size() == 1){
            this.challengerView = playerView;
        }

        return true;
    }

    //the controller must add the player to the model AFTER the server finished it's initial communication with the new client
    public synchronized void controllerAddPlayer(String nickname){
        RemotePlayerView playerView = this.playersViews.stream().filter(v -> v.getNickname().equals(nickname)).collect(Collectors.toList()).get(0);
        controller.onNicknameChosen(playerView, nickname);
    }

    public synchronized void setNumPlayers(int numPlayers){
        if(this.challengerView == null){
            throw new RuntimeException("There is no challanger in the lobby");
        }
        //the controller sets the number of players
        this.controller.onNumberOfPlayersChosen(this.challengerView, numPlayers);
    }

    public synchronized void deregisterConnection(Connection cc){
        //game is ended, goodbye
        for(RemotePlayerView v: this.playersViews){
            List<Object> message = new ArrayList<>();
            message.add("onMessage");
            message.add("disconnected");
            v.sendObjectToClient(message);
            //client has to close the game
        }
        //this.playersViews.clear();
    }
}
