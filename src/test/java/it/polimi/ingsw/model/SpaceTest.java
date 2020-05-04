package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.model.SpaceFullException;
import it.polimi.ingsw.exceptions.model.SpaceOccupiedException;
import org.junit.Test;

public class SpaceTest {

    @Test
    public void correctInitializationTest(){
        Space s = new Space();

        assert (s.getHeight().equals(Level.GROUND));
        assert (!s.isOccupied());
        assert (!s.isDome());
    }

    @Test
    public void setOccupiedTest(){
        Space s = new Space();

        s.setOccupied();

        assert (s.isOccupied());
    }

    @Test
    public void setUnoccupiedTest(){
        Space s = new Space();

        s.setOccupied();
        s.setUnoccupied();

        assert (!s.isOccupied());
    }

    @Test (expected = SpaceOccupiedException.class)
    public void destroyBlockTest(){
        Space s = new Space();
        s.setLevel(Level.DOME);

        s.setLevel(Level.LVL2);
    }

    @Test
    public void setLevelTest(){
        Space s = new Space();

        s.setLevel(Level.LVL1);

        assert (s.getHeight().equals(Level.LVL1));
    }

    @Test (expected = SpaceOccupiedException.class)
    public void levelUpOnOccupiedCellTest(){
        Space s = new Space();

        s.setOccupied();
        s.levelUp();
    }

    @Test (expected = SpaceOccupiedException.class)
    public void levelUpOnDomeCellTest(){
        Space s = new Space();

        s.setLevel(Level.DOME);
        s.levelUp();
    }

    @Test
    public void levelUpTest(){
        Space s0 = new Space();
        Space s1 = new Space();
        s1.setLevel(Level.LVL1);
        Space s2 = new Space();
        s2.setLevel(Level.LVL2);
        Space s3 = new Space();
        s3.setLevel(Level.LVL3);

        s0.levelUp();
        s1.levelUp();
        s2.levelUp();
        s3.levelUp();

        assert (s0.getHeight().equals(Level.LVL1));
        assert (s1.getHeight().equals(Level.LVL2));
        assert (s2.getHeight().equals(Level.LVL3));
        assert (s3.isDome());
    }
}
