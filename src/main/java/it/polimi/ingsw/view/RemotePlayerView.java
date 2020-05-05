package it.polimi.ingsw.view;

import it.polimi.ingsw.listeners.EventSource;
import it.polimi.ingsw.listeners.Listener;
import it.polimi.ingsw.listeners.ModelEventListener;
import it.polimi.ingsw.listeners.PlayerViewEventListener;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.observer.*;
import it.polimi.ingsw.server.ClientConnection;

import java.util.List;
import java.util.Map;

public class RemotePlayerView implements ModelEventListener, EventSource {

    //used for reacting when a message arrives from the client (assuming the client only sends strings)
    private class MessageReceiver implements Observer<Object> {
        @Override
        public void update(Object message) {
            System.out.println("Received: " + message.toString());
            receivedObject = message;
            handleMessageReceived();
        }
    }

    private ClientConnection clientConnection;
    private Object receivedObject;

    private String nickname;
    private PlayerViewEventListener controller;
    private String waitStatus;

    public RemotePlayerView(String nickname, ClientConnection cc){
        this.clientConnection = cc;
        //when a message arrives from the client, handle with my MessageReceiver
        this.clientConnection.addObserver(new MessageReceiver());

        this.nickname = nickname;
    }

    public ClientConnection getClientConnection(){
        return this.clientConnection;
    }

    //used for adding the controller as a listener for the player view events
    @Override
    public void addListener(Listener listener) {
        if (!(listener instanceof PlayerViewEventListener)) {
            throw new IllegalArgumentException("Tried to register a non-ViewEventListener to View.");
        }
        this.controller = (PlayerViewEventListener) listener;
    }

    //this method is fired when an object is received from the client
    protected void handleMessageReceived() {
        //insert code here
    }

    //used to send objects to the client through the ClientConnection
    public void sendObjectToClient(Object message){
        this.clientConnection.asyncSend(message);
    }

    //Model Events:

    @Override
    public void onBoardChanged(Board board) {

    }

    @Override
    public void onGameReady() {

    }

    @Override
    public void onGodsChosen(List<String> gods) {

    }

    @Override
    public void onPlayerAdded(String nickname, int numCurr, int numTot) {

    }

    @Override
    public void onGodSelection(List<String> gods) {

    }

    @Override
    public void onGodsSelection(List<String> gods, int numPlayers) {

    }

    @Override
    public void onStartPlayerSelection(List<String> players) {

    }

    @Override
    public void onMyInitialization(List<Coord> freeSpaces) {

    }

    @Override
    public void onMyTurn(List<Coord> selectableWorkers) {

    }

    @Override
    public void onMyAction(List<Coord> movableSpaces, Map<Level, List<Coord>> buildableSpaces, boolean canEndTurn) {

    }

    @Override
    public String getNickname() {
        return this.nickname;
    }

}
