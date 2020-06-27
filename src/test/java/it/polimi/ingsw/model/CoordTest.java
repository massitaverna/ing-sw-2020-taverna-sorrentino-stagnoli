package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.model.InvalidCoordinatesException;
import it.polimi.ingsw.exceptions.model.WorkerNotFoundException;
import org.junit.Test;

public class CoordTest {

    @Test
    public void invalidCoordinatesTest() {
        Coord c1 = new Coord(-1, 0);
        Coord c2 = new Coord(0, -1);
        Coord c3 = new Coord(-1, -1);
        Coord c4 = new Coord(5, 5);
        Coord c5 = new Coord(4, 5);
        Coord c6 = new Coord(5, 4);

        assert( !Coord.validCoord(c1) );
        assert( !Coord.validCoord(c2) );
        assert( !Coord.validCoord(c3) );
        assert( !Coord.validCoord(c4) );
        assert( !Coord.validCoord(c5) );
        assert( !Coord.validCoord(c6) );
    }

    @Test
    public void isNearTest() throws InvalidCoordinatesException {
        Coord center = new Coord(2, 2);
        Coord up = new Coord(2, 3);
        Coord upLeft = new Coord(1, 3);
        Coord upRight = new Coord(3, 3);
        Coord bottom = new Coord(2, 1);
        Coord bottomLeft = new Coord(1, 1);
        Coord bottomRight = new Coord(3, 1);
        Coord left = new Coord(1, 2);
        Coord right = new Coord(3, 2);

        assert ( !center.isNear(center) );
        assert( center.isNear(up) );
        assert( center.isNear(upLeft) );
        assert( center.isNear(upRight) );
        assert( center.isNear(bottom) );
        assert( center.isNear(bottomLeft) );
        assert( center.isNear(bottomRight) );
        assert( center.isNear(left) );
        assert( center.isNear(right) );
    }

    @Test
    public void convertStringToCoordTest(){
        String topLeft = "A1";
        String topRight = "A5";
        String bottomLeft = "E1";
        String bottomRight = "E5";
        String center = "C3";

        assert (Coord.convertStringToCoord(topLeft).equals(new Coord(0,0)));
        assert (Coord.convertStringToCoord(topRight).equals(new Coord(4,0)));
        assert (Coord.convertStringToCoord(bottomLeft).equals(new Coord(0,4)));
        assert (Coord.convertStringToCoord(bottomRight).equals(new Coord(4,4)));
        assert (Coord.convertStringToCoord(center).equals(new Coord(2,2)));
    }
}
