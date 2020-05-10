package it.polimi.ingsw.view;

import it.polimi.ingsw.listeners.ChallengerViewEventListener;
import it.polimi.ingsw.server.Connection;

import java.util.ArrayList;
import java.util.List;

public class RemoteChallengerView extends RemotePlayerView{

    public RemoteChallengerView(String nickname, Connection cc) {
        super(nickname, cc);
    }

    /*
    @Override
    protected void handleMessageReceived() {
        super.handleMessageReceived();

        List<Object> objects;
        if(super.receivedObject instanceof List)
            objects = (List<Object>)super.receivedObject;
        else {
            System.out.println("Something went wrong in handling received message");
            return;
        }

        //the first object received is always the event
        String event = (String) objects.get(0);
        switch (event) {
            case "onGodsChosen":
                List<String> gods = (List)objects.get(1);
                ((ChallengerViewEventListener)super.controller).onGodsChosen(this, gods);
                break;
            case "onStartPlayerChosen":
                String startPlayer = (String)objects.get(1);
                ((ChallengerViewEventListener)super.controller).onStartPlayerChosen(this, startPlayer);
                break;
            default:
                System.out.println("Something went wrong in handling received message");
                break;
        }
    }

   *//* @Override
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
    }*/
}
