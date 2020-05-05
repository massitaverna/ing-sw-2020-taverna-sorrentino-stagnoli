package it.polimi.ingsw.server;

import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

//                            To send coming messages (from the client) to the listener (the RemoteView)
public class ClientConnection extends Observable<Object> implements Runnable {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Lobby lobby;

    private boolean active = true;

    public ClientConnection(Socket socket, Lobby lobby) {
        this.socket = socket;
        this.lobby = lobby;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
            this.active = false;
        }
    }

    private synchronized boolean isActive(){
        return active;
    }

    private synchronized void send(Object message) {
        try{
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    public void asyncSend(final Object message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }

    public synchronized void closeConnection() {
        send("Connection closed!");
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }

    private void close() {
        closeConnection();
        System.out.println("Deregistering client...");
        lobby.deregisterConnection(this);
        System.out.println("Done!");
    }

    @Override
    public void run() {
        try{
            while(isActive()){
                //notify the Player View that a message is arrived from the client
                Object received = this.in.readObject();
                notify(received);
            }
            //disconnect the client, end of the game
            lobby.deregisterConnection(this);
        } catch (IOException | NoSuchElementException | ClassNotFoundException e) {
            System.err.println("Error!" + e.getMessage());
        }finally{
            close();
        }
    }
}
