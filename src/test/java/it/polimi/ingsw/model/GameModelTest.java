package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.model.AlreadyExistingPlayerException;
import it.polimi.ingsw.exceptions.model.GameFullException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameModelTest {

    @Test( expected = GameFullException.class )
    public void gameFullTest(){
        GameModel m = new GameModel();
        m.setNumPlayers(3);

        m.addNewPlayer(new Player("AA"));
        m.addNewPlayer(new Player("BB"));
        m.addNewPlayer(new Player("CC"));

        assert m.allPlayersArrived();

        m.addNewPlayer(new Player("DD"));
    }

    @Test( expected = AlreadyExistingPlayerException.class )
    public void alreadyExistingPlayerTest(){
        GameModel m = new GameModel();
        m.setNumPlayers(3);

        m.addNewPlayer(new Player("AA"));
        m.addNewPlayer(new Player("AA"));
    }

    @Test
    public void allPlayersHaveDifferentGodAndColor(){
        GameModel m = new GameModel();
        m.setNumPlayers(3);

        m.addNewPlayer(new Player("AA"));
        m.addNewPlayer(new Player("BB"));
        m.addNewPlayer(new Player("CC"));

        assert m.allPlayersArrived();

        //assert different colors
        ArrayList<Color> colors = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Color c = m.getCurrentPlayer().getWorkerColor();
            assert !colors.contains(c);
            colors.add(c);
            m.nextPlayer();
        }

        //assert challenger is first in queue
        assert m.getCurrentPlayer().getNickname().equals("AA");

        //assert gods set correctly
        ArrayList<String> gods = new ArrayList<>();
        gods.add("Athena");
        gods.add("Demeter");
        gods.add("Artemis");
        m.setGods(gods);
        assert gods.containsAll(m.getAvailableGods().stream().map(God::getName).collect(Collectors.toList()));

        //assert current player is the second
        assert m.getCurrentPlayer().getNickname().equals("BB");

        //assignGod removes selected god from the list
        m.assignGodToPlayer(m.getCurrentPlayer(), m.getAvailableGods().get(0) );
        m.assignGodToPlayer(m.getCurrentPlayer(), m.getAvailableGods().get(0) );
        m.assignGodToPlayer(m.getCurrentPlayer(), m.getAvailableGods().get(0) );

        //assert challenger is still the first in queue
        assert m.getCurrentPlayer().getNickname().equals("AA");

        //assert different gods
        ArrayList<String> assignedGods = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String g = m.getCurrentPlayer().getGod().getName();
            assert !assignedGods.contains(g);
            assignedGods.add(g);
            m.nextPlayer();
        }
    }
}
