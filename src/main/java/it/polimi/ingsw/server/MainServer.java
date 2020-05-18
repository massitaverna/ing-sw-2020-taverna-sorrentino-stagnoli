package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainServer {

    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private List<Socket> pendingSockets;

    private List<Lobby> lobbies;

    public MainServer() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.lobbies = new ArrayList<>();
        this.pendingSockets = new ArrayList<>();
    }

    private class ClientInitializer implements Runnable {

        Socket socket;
        ObjectOutputStream out;
        ObjectInputStream in;

        MainServer server;

        boolean valid = true;

        public ClientInitializer(Socket s, MainServer server) {
            this.server = server;
            this.socket = s;
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            }catch (IOException e){
                valid = false;
            }
        }

        @Override
        public void run() {

            int selectedLobbyIndex = -1;
            Lobby selectedLobby = null;
            boolean finished = false;

            while (!finished) {

                //send available lobbies
                int counter = 0;
                selectedLobbyIndex = -1;
                selectedLobby = null;
                try {
                    synchronized (lobbies) {
                        for (Lobby l : lobbies) {
                            out.writeObject("lobby");
                            out.flush();
                            out.writeObject(lobbies.get(counter).getPlayersNicknames());
                            out.flush();
                            out.writeObject(lobbies.get(counter).getNumPlayers());
                            out.flush();
                            out.writeObject(counter);
                            out.flush();
                            counter++;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Client disconnected while trying entering a lobby. Not registered.");
                    break;
                }

                //user is selecting an available lobby (0 to create one)
                try {
                    while (selectedLobbyIndex < 0) {
                        out.writeObject("?lobby");
                        out.flush();
                        selectedLobbyIndex = (int) in.readObject();
                        if (selectedLobbyIndex == 0) {
                            break;
                        }
                        synchronized (lobbies) {
                            if ((selectedLobbyIndex - 1) >= 0 && (selectedLobbyIndex - 1) < lobbies.size()) {
                                selectedLobby = lobbies.get(selectedLobbyIndex - 1);
                                break;
                            } else {
                                selectedLobbyIndex = -1;
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Client disconnected while trying entering a lobby. Not registered.");
                    break;
                }

                //If no lobbies or all lobbies are full:
                //if (lobbies.size() == 0 || lobbies.stream().allMatch(Lobby::isFull)) {

                //lobby has been selected
                //create new lobby
                if (selectedLobbyIndex == 0) {

                    //create one and put the client in that lobby ask him for his name and for the number of players
                    String nickname = null;
                    int numPlayers = -1;

                    //ask the client for name and numPlayers (2 or 3)
                    try {
                        while (nickname == null || (numPlayers != 2 && numPlayers != 3)) {
                            out.writeObject("challenger");
                            out.flush();
                            out.writeObject("?nickname");
                            out.flush();
                            out.writeObject(new ArrayList<String>());
                            out.flush();
                            nickname = (String) in.readObject();
                            out.writeObject("?numPlayers");
                            out.flush();
                            numPlayers = (int) in.readObject();
                        }

                        out.writeObject("ok");
                        out.flush();

                        //valid name and numPlayers, create the lobby
                        Lobby newLobby = new Lobby(server, numPlayers);
                        synchronized (lobbies) {
                            lobbies.add(newLobby);
                        }
                        //the player is the challanger
                        newLobby.addPlayer(nickname, socket, out, in);
                        newLobby.controllerAddPlayer(nickname);

                        finished = true;

                        pendingSockets.remove(this.socket);
                    }
                    //connection interrupted
                    catch (IOException | ClassNotFoundException e) {
                        System.out.println("Client disconnected while trying entering a lobby. Not registered.");
                        break;
                    }

                }
                //add player to the lobby
                else {
                    //find the first free lobby
                    //Lobby firstFreeLobby = lobbies.stream().filter(l -> !l.isFull()).collect(Collectors.toList()).get(0);

                    if (selectedLobby != null) {

                        //ask the client until he choose a valid nickname
                        String nickname = null;
                        boolean validNickname = false;

                        try {
                            out.writeObject("!challenger");
                            out.flush();
                            while (nickname == null || !validNickname) {

                                //another player could has been faster than this player
                                if (selectedLobby.isFull()) {
                                    out.writeObject("fullLobby");
                                    out.flush();
                                    break;
                                }
                                out.writeObject("?nickname");
                                out.flush();
                                List<String> nicknames = selectedLobby.getPlayersNicknames();
                                out.writeObject(nicknames);
                                out.flush();

                                nickname = (String) in.readObject();

                                //if addPlayer returns false, it means that the a player with that name is already in the lobby, the name must be re-inserted
                                //note that if two clients are trying to connect in the same moment, they could choose the same nickname
                                //only the faster client will manage to connect

                                out.flush();

                                validNickname = selectedLobby.addPlayer(nickname, socket, out, in);

                                if (validNickname) {
                                    out.writeObject("ok");
                                    out.flush();
                                    finished = true;
                                    selectedLobby.controllerAddPlayer(nickname);
                                } else { //lobby is full
                                    out.writeObject("fullLobby");
                                    out.flush();
                                    break;
                                }

                                pendingSockets.remove(this.socket);
                            }

                        }//connection interrupted
                        catch (IOException | ClassNotFoundException e) {
                            System.out.println("Client disconnected while trying entering a lobby. Not registered.");
                            break;
                        }

                    }//selectedLobby != null

                }//else add to lobby

            }// main loop

        }//void run

    }//class

    public void runServer() throws IOException {
        while(true) {
            try {
                Socket newSocket = serverSocket.accept();
                newSocket.setKeepAlive(true);
                pendingSockets.add(newSocket);
                executor.submit(new ClientInitializer(newSocket, this));
            } catch (IOException e) {
                System.out.println("Closing server...");
                break;
            }
        }

        //shutdown all threads
        executor.shutdown();
        serverSocket.close();
        //close and shutdown all lobbies
        synchronized (lobbies) {
            for (Lobby l : lobbies) {
                l.closeConnections();
            }
            //close all pending sockets
            for (Socket s: pendingSockets){
                s.close();
            }
        }
    }

    public void removeLobby(Lobby lobby){
        System.out.println("Server: Closing lobby " + lobby.toString());
        synchronized (lobbies) {
            this.lobbies.remove(lobby);
        }
    }
}
