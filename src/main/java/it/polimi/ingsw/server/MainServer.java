package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MainServer {

    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newCachedThreadPool();

    private List<Lobby> lobbies;

    public MainServer() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.lobbies = new ArrayList<>();
    }

    private class ClientInitializer implements Runnable {

        Socket socket;
        ObjectOutputStream out;
        ObjectInputStream in;

        boolean valid = true;

        public ClientInitializer(Socket s) {
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
            if (valid) {

                //If no lobbies or all lobbies are full:
                if (lobbies.size() == 0 || lobbies.stream().allMatch(Lobby::isFull)) {

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
                        Lobby newLobby = new Lobby();
                        synchronized (lobbies) {
                            lobbies.add(newLobby);
                        }
                        //the player is the challanger
                        newLobby.addPlayer(nickname, socket, out, in);
                        newLobby.setNumPlayers(numPlayers);

                    }
                    //connection interrupted
                    catch (IOException | ClassNotFoundException e) {
                        System.out.println("Client disconnected while trying entering a lobby. Not registered.");
                    }

                }
                //if there are lobbies not full
                else {
                    //find the first free lobby
                    Lobby firstFreeLobby = lobbies.stream().filter(l -> !l.isFull()).collect(Collectors.toList()).get(0);

                    //ask the client until he choose a valid nickname
                    String nickname = null;
                    boolean validNickname = false;
                    try {
                        out.writeObject("!challenger");
                        out.flush();
                        while (nickname == null || !validNickname) {

                            //another player could has been faster than this player
                            if (firstFreeLobby.isFull()) {
                                out.writeObject("fullLobby");
                                out.flush();
                                break;
                            }

                            out.writeObject("?nickname");
                            out.flush();

                            List<String> nicknames = firstFreeLobby.getPlayersNicknames();
                            out.writeObject(nicknames);
                            out.flush();

                            nickname = (String) in.readObject();

                            //if addPlayer returns false, it means that the a player with that name is already in the lobby, the name must be re-inserted
                            //note that if two clients are trying to connect in the same moment, they could choose the same nickname
                            //only the faster client will manage to connect

                            out.flush();

                            validNickname = firstFreeLobby.addPlayer(nickname, socket, out, in);

                            if (validNickname) {
                                out.writeObject("ok");
                                out.flush();
                            }
                        }
                    }
                    //connection interrupted
                    catch (IOException | ClassNotFoundException e) {
                        System.out.println("Client disconnected while trying entering a lobby. Not registered.");
                    }

                }
            }
        }
    }

    public void runServer() throws IOException {
        while(true) {
            try {
                Socket newSocket = serverSocket.accept();
                executor.submit(new ClientInitializer(newSocket));
            } catch (IOException e) {
                System.out.println("Server socket closed.");
                break;
            }
        }
        executor.shutdown();
        serverSocket.close();
    }
}
