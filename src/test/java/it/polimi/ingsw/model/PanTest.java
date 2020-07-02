package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.PrepareModelException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PanTest extends GameRulesTest {

    @Before
    public void setInitialSituation() {
        try {
            model = TestUtils.prepareModel("pan_test");
            before = model.getBoard();
        }
        catch (PrepareModelException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            fail("Couldn't prepare model as specified in configuration file.");
        }
    }


    //-------------------------------TEST SECTION---------------------------------

    @Test
    public void effect() {
        /*
        Test he can win by moving down two or more levels
        */

        src = Coord.convertStringToCoord("B1");
        dest = Coord.convertStringToCoord("B2");

        //Pre-action
        nextStep();
        model.setWorkerChoice(src);
        nextStep();
        setMove(dest);

        //Do the build
        dest = Coord.convertStringToCoord("C1");
        nextStep();
        setBuild(dest, Level.DOME);

        //Check what changed
        Board after = model.getBoard();
        assertTrue(after.getSpace(dest).isDome());
        assertEquals(before.getSpace(dest).getHeight(), after.getSpace(dest).getHeight());
    }
}
