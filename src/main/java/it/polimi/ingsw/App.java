package it.polimi.ingsw;

import it.polimi.ingsw.Model.*;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Player p1, p2, p3;
        p1 = new Player("qwerty", null, Color.RED);
        p2 = new Player("asdf", null, Color.GREEN);
        p3 = new Player("zxc", null, Color.BLUE);

        ArrayList<Player> playersList = new ArrayList<>();
        playersList.add(p1);
        playersList.add(p2);
        playersList.add(p3);

        Game g = new Game(playersList);
        try {
            g.Start();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
