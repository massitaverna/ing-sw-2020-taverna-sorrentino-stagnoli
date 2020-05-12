package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientCLI;
import it.polimi.ingsw.server.Connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

//TODO: async read from server

public class ClientMainCLI
{
    public static void main( String[] args ) {

        String ip = "127.0.0.1";
        int port = 12345;

        Scanner s = new Scanner(System.in);
        Socket socket = null;
        ObjectInputStream in;
        ObjectOutputStream out;

        ClientCLI cli = null;

        try {
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Connection to the server failed.");
            return;
        }

        boolean challenger = false;
        int numPlayers = 0;
        String nickname = "";

        boolean finished = false;
        try {
            while (!finished) {
                String message = (String) in.readObject();
                switch (message) {
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
                            nickname = s.nextLine();
                            if (nicknamesInLobby.contains(nickname))
                                System.out.println("Invalid input: nickname already in the lobby.");
                            else if (nickname.equals(""))
                                System.out.println("Invalid input: nickname can't be an empty string.");
                        }
                        out.writeObject(nickname);
                        out.flush();
                        break;

                    case "fullLobby":
                        System.out.println("The lobby is full");
                        finished = true;
                        break;

                    //entro in una lobby
                    case "ok":
                        finished = true;
                        cli = new ClientCLI(new Connection(socket, out, in), challenger);
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
        try {
            System.out.println("Premere un tasto per continuare");
            in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
