package it.polimi.ingsw.client;

import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.Connection;

public class ClientCLI {
    //used for reacting when a message arrives from the server
    private class MessageReceiver implements Observer<Object> {
        @Override
        public void update(Object message) {
            System.out.println("Received: " + message.toString());
            receivedObject = message;
            handleMessageReceived();
        }
    }

    private Object receivedObject;
    private Connection serverConnection;
    private String nickname;

    public ClientCLI(Connection serverConnection){
        this.serverConnection = serverConnection;
        this.serverConnection.addObserver(new MessageReceiver());
    }

    private void handleMessageReceived(){}

    public void run(){}
}