package it.polimi.ingsw;

import it.polimi.ingsw.server.MainServer;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) {
        try {
            MainServer server = new MainServer();
            System.out.println("Starting server...");
            server.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
