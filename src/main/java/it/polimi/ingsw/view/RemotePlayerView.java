package it.polimi.ingsw.view;

import it.polimi.ingsw.exceptions.controller.IllegalPlayerException;
import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.observer.*;
import it.polimi.ingsw.server.Connection;
import org.hamcrest.CoreMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private Connection clientConnection;
    protected Object receivedObject;

    private String nickname;

    protected PlayerViewEventListener controller;

    public RemotePlayerView(String nickname, Connection cc){
        this.clientConnection = cc;
        //when a message arrives from the client, handle with my MessageReceiver
        this.clientConnection.addObserver(new MessageReceiver());

        this.nickname = nickname;
    }

    public Connection getClientConnection(){
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
        List<Object> objects;
        if(receivedObject instanceof List)
            objects = (List<Object>)receivedObject;
        else {
            System.out.println("Something went wrong in handling received message");
            return;
        }

        //the first object received is always the event
        String event = (String) objects.get(0);
        switch (event) {
            //player events
            case "onGodChosen":
                String god = (String) objects.get(1);
                try{
                    controller.onGodChosen(this, god);
                }catch (IllegalPlayerException e){ System.out.println("Illegal Player"); }
                break;
            case "onWorkerInitialization":
                Coord choice = (Coord) objects.get(1);
                controller.onWorkerInitialization(this, choice);
                break;
            case "onWorkerChosen":
                Coord workerPos = (Coord) objects.get(1);
                controller.onWorkerChosen(this, workerPos);
                break;
            case "onMoveChosen":
                Coord moveChoice = (Coord) objects.get(1);
                controller.onMoveChosen(this, moveChoice);
                break;
            case "onBuildChosen":
                Coord buildChoice = (Coord) objects.get(1);
                Level level = (Level) objects.get(2);
                controller.onBuildChosen(this, buildChoice, level);
                break;
            case "skipAction":
                controller.skipAction(this);
                break;
            //challenger events
            case "onGodsChosen":
                List<String> gods = (List)objects.get(1);
                ((ChallengerViewEventListener)controller).onGodsChosen(this, gods);
                break;
            case "onStartPlayerChosen":
                String startPlayer = (String)objects.get(1);
                ((ChallengerViewEventListener)controller).onStartPlayerChosen(this, startPlayer);
                break;
            //client ping check
            case "onPing":
                //TODO: client ping succesful
                break;
            default:
                System.out.println("Event message not recognized.");
                break;
        }
    }

    //used to send objects to the client through the ClientConnection
    public void sendObjectToClient(Object message){
        this.clientConnection.asyncSend(message);
    }

    //Model Events:
    @Override
    public void onBoardChanged(Board board) {
        List<Object> objects = new ArrayList<>();
        objects.add("onBoardChanged");
        objects.add(board);
        sendObjectToClient(objects);
    }

    @Override
    public void onGameReady() {
        List<Object> objects = new ArrayList<>();
        objects.add("onGameReady");
        sendObjectToClient(objects);
    }

    @Override
    public void onGodsChosen(List<String> gods) {
        List<Object> objects = new ArrayList<>();
        objects.add("onGodsChosen");
        objects.add(gods);
        sendObjectToClient(objects);
    }

    @Override
    public void onPlayerAdded(String nickname, int numCurr, int numTot) {
        List<Object> objects = new ArrayList<>();
        objects.add("onPlayerAdded");
        objects.add(nickname);
        objects.add(numCurr);
        objects.add(numTot);
        sendObjectToClient(objects);
    }

    @Override
    public void onMessage(String message) {
        List<Object> objects = new ArrayList<>();
        objects.add("onMessage");
        objects.add(message);
        sendObjectToClient(objects);
    }

    @Override
    public void onGodSelection(List<String> gods) {
        List<Object> objects = new ArrayList<>();
        objects.add("onGodSelection");
        objects.add(gods);
        sendObjectToClient(objects);
    }

    @Override
    public void onGodsSelection(List<String> gods, int numPlayers) {
        List<Object> objects = new ArrayList<>();
        objects.add("onGodsSelection");
        objects.add(gods);
        objects.add(numPlayers);
        sendObjectToClient(objects);
    }

    @Override
    public void onStartPlayerSelection(List<String> players) {
        List<Object> objects = new ArrayList<>();
        objects.add("onStartPlayerSelection");
        objects.add(players);
        sendObjectToClient(objects);
    }

    @Override
    public void onMyInitialization(List<Coord> freeSpaces) {
        List<Object> objects = new ArrayList<>();
        objects.add("onMyInitialization");
        objects.add(freeSpaces);
        sendObjectToClient(objects);
    }

    @Override
    public void onMyTurn(List<Coord> selectableWorkers) {
        List<Object> objects = new ArrayList<>();
        objects.add("onMyTurn");
        objects.add(selectableWorkers);
        sendObjectToClient(objects);
    }

    @Override
    public void onMyAction(List<Coord> movableSpaces, Map<Level, List<Coord>> buildableSpaces, boolean canEndTurn) {
        List<Object> objects = new ArrayList<>();
        objects.add("onMyAction");
        objects.add(movableSpaces);
        objects.add(buildableSpaces);
        objects.add(canEndTurn);
        sendObjectToClient(objects);
    }

    @Override
    public void onWin(String winner) {
        List<Object> objects = new ArrayList<>();
        objects.add("onWin");
        objects.add(winner);
        sendObjectToClient(objects);
    }

    @Override
    public String getNickname() {
        return this.nickname;
    }

}
