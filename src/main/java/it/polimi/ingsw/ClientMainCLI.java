package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientCLI;
import it.polimi.ingsw.server.Connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

//TODO: async read from server

public class ClientMainCLI
{
    public static void main( String[] args ) {

        String ip = "127.0.0.1";
        int port = 12345;

        Scanner s = new Scanner(System.in);
        Scanner ss = new Scanner(System.in);
        Socket socket = null;
        ObjectInputStream in;
        ObjectOutputStream out;

        ClientCLI cli = null;

        try {
            socket = new Socket(ip, port);
            socket.setKeepAlive(true);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Connection to the server failed.");
            return;
        }

        boolean finished = false;

        //lobby selection
        Map<Integer, List<String>> availableLobbies = new HashMap<>();
        Map<Integer, Integer> availableLobbiesPlayersCount = new HashMap<>();

        //entering a lobby
        boolean challenger = false;
        int numPlayers = 0;
        String nickname = "";

        try {
            while (!finished) {
                String message = (String) in.readObject();
                switch (message) {

                    case "lobby":
                        List<String> playersInLobby = (List<String>) in.readObject();
                        int maxPlayers = (int)in.readObject();
                        int lobbyNum = (int) in.readObject();
                        availableLobbies.put(lobbyNum, playersInLobby);
                        availableLobbiesPlayersCount.put(lobbyNum, maxPlayers);
                        break;

                    case "?lobby":
                        System.out.println("Choose the lobby to join (insert a number) :");
                        System.out.println("0 - Create new lobby");
                        //print lobby info
                        for(Integer i: availableLobbies.keySet()){
                            int maxP, currentP;
                            maxP = availableLobbiesPlayersCount.get(i);
                            currentP = availableLobbies.get(i).size();
                            System.out.print((i+1) + " - Players (" + currentP + "/" + maxP + "): ");
                            for(String name: availableLobbies.get(i)){
                                System.out.print("\"" + name + "\" ");
                            }
                            System.out.println("");
                        }
                        //user choice
                        int choice = -1;
                        while (choice < 0) {
                            try {
                                choice = s.nextInt();
                            } catch (Exception e) {
                                System.out.println("Insert a digit.");
                            }
                            if(choice == 0){
                                break;
                            }
                            if (choice < 0 || choice > availableLobbies.keySet().size() || availableLobbiesPlayersCount.get(choice-1) == availableLobbies.get(choice-1).size()) {
                                System.out.println("Invalid input, try again");
                                choice = -1;
                            }
                        }
                        out.writeObject(choice);
                        out.flush();
                        break;

                    case "challenger":
                        System.out.println("You are the challenger");
                        challenger = true;
                        break;

                    case "!challenger":
                        challenger = false;
                        break;

                    case "?numPlayers":
                        System.out.println("Insert the number of opponents (1 or 2):");
                        while (numPlayers != 1 && numPlayers != 2) {
                            try {
                                numPlayers = s.nextInt();
                            } catch (Exception e) {
                                System.out.println("Insert a digit.");
                                s.next();
                            }
                            if (numPlayers != 1 && numPlayers != 2)
                                System.out.println("Invalid input, try again");
                        }
                        numPlayers++;
                        out.writeObject(numPlayers);
                        out.flush();
                        break;

                    case "?nickname":
                        List<String> nicknamesInLobby = (List<String>) in.readObject();
                        System.out.println("What's your nickname?");
                        if(nicknamesInLobby.size() > 0) {
                            System.out.println("Players already in the lobby: ");
                            nicknamesInLobby.forEach(System.out::println);
                        }
                        while (nicknamesInLobby.contains(nickname) || nickname.equals("")) {
                            s.reset();
                            nickname = ss.nextLine();
                            if (nicknamesInLobby.contains(nickname))
                                System.out.println("Invalid input: nickname already in the lobby.");
                            else if (nickname.equals(""))
                                System.out.println("Invalid input: nickname can't be an empty string.");
                        }
                        out.writeObject(nickname);
                        out.flush();
                        break;

                    case "fullLobby":
                        System.out.println("The lobby is full, please select another one:");
                        availableLobbies = new HashMap<>();
                        availableLobbiesPlayersCount = new HashMap<>();
                        break;

                    //entro in una lobby
                    case "ok":
                        finished = true;
                        cli = new ClientCLI(new Connection(socket, out, in), challenger, nickname);
                        cli.run();
                        break;

                    default:
                        System.out.println("Something went wrong. Bye!");
                        finished = true;
                        break;

                }
            }
        }catch (IOException | ClassNotFoundException e){
            System.out.println("Connection went down while trying to connect.");
        }finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
            }
        }

        System.out.println("Game finished. Closing application...");
        if(cli != null) {
            cli.stop();
        }
        System.out.println("Press any key to continue...");
        ss.nextLine();
    }
}
