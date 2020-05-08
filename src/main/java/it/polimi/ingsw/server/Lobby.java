package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.RealController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.view.RemoteChallengerView;
import it.polimi.ingsw.view.RemotePlayerView;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lobby {

    private GameModel model;
    private RealController controller;

    private List<RemotePlayerView> playersViews;
    private RemotePlayerView challengerView;

    private ExecutorService executor = Executors.newCachedThreadPool();

    public Lobby(){
        this.model = new GameModel();
        this.controller = new RealController(this.model);
        this.playersViews = new ArrayList<>();
    }

    public List<String> getPlayersNicknames(){
        List<String> result = new ArrayList<>();
        for(RemotePlayerView v: this.playersViews){
            result.add(v.getNickname());
        }
        return result;
    }

    public synchronized boolean addPlayer(String nickname, Socket socket){

        //if name is null or is already present, return false
        if(nickname == null || nickname.equals("") || this.playersViews.stream().anyMatch(x -> x.getNickname().equals(nickname)) )
            return false;

        if(socket.isClosed())
            return false;

        //if it is the first player coming, he is the challenger
        if(this.playersViews.size() == 0){
            this.challengerView = new RemoteChallengerView(nickname, new Connection(socket, this));
            //add to the list of players
            this.playersViews.add(challengerView);
            //pass the controller to make the challenger view to add it as a listener
            this.challengerView.addListener(controller);
            //the challenger view is a listener of the model
            this.controller.onNicknameChosen(challengerView, nickname);
            //start a separate thread waiting for client messages
            this.executor.submit(challengerView.getClientConnection());
        }
        //if not the first player, he is a normal player
        else {
            RemotePlayerView playerView = new RemotePlayerView(nickname, new Connection(socket, this));
            //add to the list of players
            this.playersViews.add(playerView);
            //pass the controller to make the view to add it as listener
            playerView.addListener(controller);
            //the player view is a listener of the model
            this.controller.onNicknameChosen(playerView, nickname);
            //start a separate thread waiting for client messages
            this.executor.submit(playerView.getClientConnection());
        }

        return true;
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
            v.sendObjectToClient("AZZZ");
            //client has to close the game
        }
    }

    public boolean isFull(){
        return this.playersViews.size() == this.model.getNumPlayers();
    }
}
