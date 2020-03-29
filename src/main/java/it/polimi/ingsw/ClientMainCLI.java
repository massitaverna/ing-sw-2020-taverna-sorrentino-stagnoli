package it.polimi.ingsw;

import it.polimi.ingsw.view.Client;

public class ClientMainCLI
{
    private static String ip = "127.0.0.1";
    private static int port = 12345;

    public static void main( String[] args ) {
        Client client = new Client(ip, port);
    }
}
