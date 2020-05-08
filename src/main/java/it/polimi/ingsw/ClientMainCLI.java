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
    public static void main( String[] args ) throws IOException, ClassNotFoundException{

        String ip = "127.0.0.1";
        int port = 12345;

        Scanner s = new Scanner(System.in);
        Socket socket = new Socket(ip, port);
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

        String challenger = (String) in.readObject();
        if (challenger.equals("challenger")){ // YOU ARE THE CHALLENGER
            // NICKNAME
            in.readObject(); // should be "?nickname"
            System.out.println("What's your nickname?");
            String input = "";
            while (input.equals("")){
                System.out.println("Your nickname can't be an empty string");
                input = s.nextLine();
            }
            out.writeUTF(input);
            out.flush();

            // OPPONENTS
            in.readObject(); // should be "?numPlayers"
            System.out.println("Choose the number of opponents (1 or 2):");
            int numInput = 0;
            while(numInput != 1 && numInput != 2){
                try {
                    numInput = s.nextInt();
                } catch (Exception e) {
                    System.out.println("Insert a digit.");
                }
                if (numInput != 1 && numInput != 2)
                    System.out.println("Invalid input, try again");
            }
            numInput++;
            out.writeInt(numInput);
            out.flush();
        } else { // YOU ARE NOT THE CHALLENGER
            Object o = in.readObject();
            List<String> nicknamesInLobby;
            if (o instanceof List){
                nicknamesInLobby = (List<String>) o;
                in.readObject(); // should be "?nickname"
                System.out.println("What's your nickname?");
                String input = "";
                while (nicknamesInLobby.contains(input) || input.equals("")){
                    input = s.nextLine();
                    if(nicknamesInLobby.contains(input))
                        System.out.println("Invalid input: nickname already in the lobby.");
                    else if (input.equals(""))
                        System.out.println("Invalid input: nickname can't be an empty string.");
                }
                out.writeObject(input);
                out.flush();
            } else {
                System.out.println("Something went wrong. Bye!");
                return;
            }
        }
        if(in.readObject().equals("ok")){
            ClientCLI cli = new ClientCLI(new Connection(socket));
            cli.run();
        } else {
            System.out.println("Something went wrong. Bye!");
        }
    }
}
