package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.Random;

public class Challanger{

    public static ArrayList<IGod> ChooseGods(){
        System.out.println("Challanger is choosing gods for the game");
        return new ArrayList<IGod>();
    }

    //sceglie un giocatore iniziale e lo rimuove dalla lista
    public static Player ChooseStartPlayer(ArrayList<Player> playerList){
        int rand = new Random().nextInt(playerList.size());
        Player startPlayer = playerList.get(rand);
        playerList.remove(startPlayer);

        return startPlayer;
    }
}
