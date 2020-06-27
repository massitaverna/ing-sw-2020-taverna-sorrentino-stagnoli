package it.polimi.ingsw.server;

import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

//                            To send coming messages (from the client) to the listener (the RemoteView)
public class Connection extends Observable<Object> implements Runnable {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Lobby lobby;

    private boolean active = true;

    public Connection(Socket socket, Lobby lobby, ObjectOutputStream o, ObjectInputStream i) {
        this.socket = socket;
        this.lobby = lobby;
        this.out = o;
        this.in = i;
    }

    public Connection(Socket socket, ObjectOutputStream o, ObjectInputStream i){
        this.socket = socket;
        this.out = o;
        this.in = i;
    }

    /**
     * To get a reference to the ObjectOutputStream object
     * @return
     */
    public synchronized ObjectOutputStream getOutputStream(){
        return this.out;
    }

    /**
     * To get a reference to the ObjectInputStream object
     * @return
     */
    public synchronized ObjectInputStream getInputStream(){
        return this.in;
    }

    private synchronized boolean isActive(){
        return active;
    }

    private synchronized void send(Object message) {
        if(isActive()) {
            try {
                out.reset();
                out.writeUnshared(message);
                out.flush();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Start a new thread to asynchronously send messages through the socket
     * @param message
     */
    public void asyncSend(final Object message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }

    /**
     * To close the connection
     */
    public synchronized void closeConnection() {
        if(isActive()) {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error when closing socket!");
            }
            active = false;
            System.out.println("Socket " + this.toString() + ": Closing connection");
        }
    }

    /**
     * To listen for coming messages from the socket.
     */
    @Override
    public void run() {
        try{
            while(isActive()){
                //notify the Player View that a message is arrived from the client
                Object received = this.in.readObject();
                notify(received);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            //if this connection is running on the server, tell the lobby to close all the connections
        }finally{
            if (lobby != null) {
                lobby.closeConnections();
            }
            closeConnection();
        }
    }
}
