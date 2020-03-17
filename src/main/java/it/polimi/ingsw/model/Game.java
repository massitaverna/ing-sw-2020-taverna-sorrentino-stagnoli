package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private int numPlayers;
    private List<Player> queue;

    public Game(int numPlayers, Player challenger){
        this.numPlayers = numPlayers;
        this.queue = new ArrayList<Player>();
        this.queue.add(challenger);
    }

    public void addPlayer(Player player){
        queue.add(player);
    }

    public List<Player> getQueue(){
        return null;
    }

}
