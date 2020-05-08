package it.polimi.ingsw.view;

import it.polimi.ingsw.server.Connection;

import java.util.ArrayList;
import java.util.List;

public class RemoteChallengerView extends RemotePlayerView{

    public RemoteChallengerView(String nickname, Connection cc) {
        super(nickname, cc);
    }

    @Override
    protected void handleMessageReceived() {
        //insert code here
    }

    @Override
    public void onGodsSelection(List<String> gods, int numPlayers) {
        List<Object> objects = new ArrayList<>();
        objects.add("OnGodsSelection");
        objects.add(gods);
        objects.add(numPlayers);
        sendObjectToClient(objects);
    }

    @Override
    public void onStartPlayerSelection(List<String> players) {
        List<Object> objects = new ArrayList<>();
        objects.add("OnStartPlayerSelection");
        objects.add(players);
        sendObjectToClient(objects);
    }
}
