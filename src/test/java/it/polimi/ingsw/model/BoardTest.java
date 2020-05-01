/*
//TODO: Decommentare e riscrivere il test coi nuovi metodi introdotti
package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class BoardTest {

    //Test getWorkerByPosition() returns the correct positions of the workers
    @Test ( expected = WorkerNotFoundException.class )
    public void getWorkerByPositionTest() throws WorkerNotFoundException {
        Board b = new Board();
        Player p1 = new Player("Lucio");
        Player p2 = new Player("Asdrogonio");
        Player p3 = new Player("Timburlaldo");
        b.addWorker(p1.getWorker(0));
        b.addWorker(p1.getWorker(1));
        b.addWorker(p2.getWorker(0));
        b.addWorker(p2.getWorker(1));
        b.addWorker(p3.getWorker(0));
        b.addWorker(p3.getWorker(1));

        b.initializeWorker(p1.getWorker(0), new Coord(0, 0));
        b.initializeWorker(p1.getWorker(1), new Coord(1, 0));
        b.initializeWorker(p2.getWorker(0), new Coord(0, 1));
        b.initializeWorker(p2.getWorker(1), new Coord(1, 1));
        b.initializeWorker(p3.getWorker(0), new Coord(2, 0));
        b.initializeWorker(p3.getWorker(1), new Coord(2, 1));

        //check the returned worker is the same worker on that position
        assert(b.getWorkerByPosition(new Coord(0, 0)) == p1.getWorker(0));
        assert(b.getWorkerByPosition(new Coord(1, 0)) == p1.getWorker(1));
        assert(b.getWorkerByPosition(new Coord(0, 1)) == p2.getWorker(0));
        assert(b.getWorkerByPosition(new Coord(1, 1)) == p2.getWorker(1));
        assert(b.getWorkerByPosition(new Coord(2, 0)) == p3.getWorker(0));
        assert(b.getWorkerByPosition(new Coord(2, 1)) == p3.getWorker(1));

        //this should throw the worker not found exception
        b.getWorkerByPosition(new Coord(5, 5));

    }

    //Test getUnoccupiedSpaces works correctly
    @Test
    public void getUnoccupiedSpacesTest() {
        Board b = new Board();
        Player p1 = new Player("Lucio");
        Player p2 = new Player("Asdrogonio");
        Player p3 = new Player("Timburlaldo");
        b.addWorker(p1.getWorker(0));
        b.addWorker(p1.getWorker(1));
        b.addWorker(p2.getWorker(0));
        b.addWorker(p2.getWorker(1));
        b.addWorker(p3.getWorker(0));
        b.addWorker(p3.getWorker(1));

        //when board is created, it is empty and the count of unoccupied spaces should be 25
        assert(b.getUnoccupiedSpaces().size() == 25);

        b.initializeWorker(p1.getWorker(0), new Coord(0, 0));
        assert(b.getUnoccupiedSpaces().size() == 24);
        b.initializeWorker(p1.getWorker(1), new Coord(1, 0));
        assert(b.getUnoccupiedSpaces().size() == 23);
        b.initializeWorker(p2.getWorker(0), new Coord(0, 1));
        assert(b.getUnoccupiedSpaces().size() == 22);
        b.initializeWorker(p2.getWorker(1), new Coord(1, 1));
        assert(b.getUnoccupiedSpaces().size() == 21);
        b.initializeWorker(p3.getWorker(0), new Coord(2, 0));
        assert(b.getUnoccupiedSpaces().size() == 20);
        b.initializeWorker(p3.getWorker(1), new Coord(2, 1));
        assert(b.getUnoccupiedSpaces().size() == 19);
    }

    //Test adding an already added worker throws an exception
    @Test ( expected = IllegalStateException.class )
    public void addAlreadyAddedWorker() throws IllegalStateException {
        Board b = new Board();
        Player p1 = new Player("Lucio");
        Player p2 = new Player("Asdrogonio");

        b.addWorker(p1.getWorker(0));
        b.addWorker(p1.getWorker(1));
        b.addWorker(p2.getWorker(0));
        b.addWorker(p2.getWorker(1));

        //this should throw the exception
        b.addWorker(p1.getWorker(0));
    }

    //test exception is thrown when initializing an already placed worker
    @Test ( expected = IllegalStateException.class )
    public void initializeAlreadyPlacedWorker() throws IllegalArgumentException, IllegalStateException {
        Board b = new Board();
        Player p1 = new Player("Lucio");
        Player p2 = new Player("Asdrogonio");
        b.addWorker(p1.getWorker(0));
        b.addWorker(p1.getWorker(1));
        b.addWorker(p2.getWorker(0));
        b.addWorker(p2.getWorker(1));

        b.initializeWorker(p1.getWorker(0), new Coord(0, 0));

        //this should throw the exception
        b.initializeWorker(p1.getWorker(0), new Coord(0, 1));
    }

    //test exception is thrown when initializing a worker that is not part of the game (not added to the board)
    @Test ( expected = IllegalArgumentException.class )
    public void initializeWorkerNotPartOfTheGame() throws IllegalArgumentException, IllegalStateException {
        Board b = new Board();
        Player p1 = new Player("Lucio");
        Player p2 = new Player("Asdrogonio");
        Player p3 = new Player("DSSDSD");
        b.addWorker(p1.getWorker(0));
        b.addWorker(p1.getWorker(1));
        b.addWorker(p2.getWorker(0));
        b.addWorker(p2.getWorker(1));

        Worker invalidWorker = new Worker(p3);
        b.initializeWorker(invalidWorker, new Coord(0, 0));
    }

    //test that after initialization all workers previously added to the board have valid coordinates
    @Test
    public void noNullPositionsOfWorkersAfterInitialization() throws IllegalArgumentException, IllegalStateException {
        Board b = new Board();
        Player p1 = new Player("Lucio");
        Player p2 = new Player("Asdrogonio");
        Player p3 = new Player("DSSDSD");
        b.addWorker(p1.getWorker(0));
        b.addWorker(p1.getWorker(1));
        b.addWorker(p2.getWorker(0));
        b.addWorker(p2.getWorker(1));
        b.addWorker(p3.getWorker(0));
        b.addWorker(p3.getWorker(1));

        b.initializeWorker(p1.getWorker(0), new Coord(0, 0));
        b.initializeWorker(p1.getWorker(1), new Coord(1, 0));
        b.initializeWorker(p2.getWorker(0), new Coord(1, 1));
        b.initializeWorker(p2.getWorker(1), new Coord(2, 0));
        b.initializeWorker(p3.getWorker(0), new Coord(3, 0));
        b.initializeWorker(p3.getWorker(1), new Coord(3, 4));

        for(Worker w: b.getAllWorkers()){
            assert( !(w.getPosition()==null) && Coord.validCoord(w.getPosition()));
        }
    }

    //test exception is thrown when initializing on already occupied space
    @Test ( expected = IllegalStateException.class )
    public void forceInitializationOnOccupiedSpace() throws IllegalArgumentException, IllegalStateException {
        Board b = new Board();
        Player p1 = new Player("Lucio");
        Player p2 = new Player("Asdrogonio");
        Player p3 = new Player("DSSDSD");
        b.addWorker(p1.getWorker(0));
        b.addWorker(p1.getWorker(1));
        b.addWorker(p2.getWorker(0));
        b.addWorker(p2.getWorker(1));
        b.addWorker(p3.getWorker(0));
        b.addWorker(p3.getWorker(1));

        b.initializeWorker(p1.getWorker(0), new Coord(0, 0));
        b.initializeWorker(p1.getWorker(1), new Coord(0, 0));
    }

    //test exception is thrown when trying to move a worker that has not been initialized
    @Test ( expected = IllegalWorkerActionException.class )
    public void cannotMoveUninitializedWorker() throws InvalidCoordinatesException, SpaceFullException, SpaceOccupiedException, IllegalWorkerActionException{
        Board b = new Board();
        Player p1 = new Player("Lucio");

        b.addWorker(p1.getWorker(0));
        b.addWorker(p1.getWorker(1));


        //initialize workers
        b.initializeWorker(p1.getWorker(0), new Coord(0, 0));

        b.workerMove(p1.getWorker(1), new Coord(2, 0));
    }

    //Test exception is thrown when moving a worker to an occupied space
    @Test ( expected = SpaceOccupiedException.class )
    public void notTwoWorkersOnSameSpace() throws InvalidCoordinatesException, SpaceFullException, SpaceOccupiedException, IllegalWorkerActionException {

        Random rand = new Random();
        Board b = new Board();
        Player p1 = new Player("Lucio");
        Player p2 = new Player("Asdrogonio");
        Player p3 = new Player("Timburlaldo");
        b.addWorker(p1.getWorker(0));
        b.addWorker(p1.getWorker(1));
        b.addWorker(p2.getWorker(0));
        b.addWorker(p2.getWorker(1));
        b.addWorker(p3.getWorker(0));
        b.addWorker(p3.getWorker(1));

        //initialize workers
        b.initializeWorker(p1.getWorker(0), new Coord(0, 0));
        b.initializeWorker(p1.getWorker(1), new Coord(1, 0));
        b.initializeWorker(p2.getWorker(0), new Coord(0, 1));
        b.initializeWorker(p2.getWorker(1), new Coord(1, 1));
        b.initializeWorker(p3.getWorker(0), new Coord(2, 0));
        b.initializeWorker(p3.getWorker(1), new Coord(2, 1));

        //, forza il movimento di un worker dove si trova un altro worker.
        int randomWorkerIndex = rand.nextInt(b.getAllWorkers().length);
        Worker randomWorker = b.getAllWorkers()[randomWorkerIndex];
        Coord otherWorkerPosition = b.getAllWorkers()[(randomWorkerIndex==0?randomWorkerIndex+1:(randomWorkerIndex==5?randomWorkerIndex-1:randomWorkerIndex+1))].getPosition();
        b.workerMove(randomWorker, otherWorkerPosition);
    }

    //Test Board.getMovableSpaceAround method does not give occupied (there is a worker) or full (there is a Dome) space coordinates
    //No exceptions expected from this test
    @Test
    public void notMoveOnOccupiedOrFullSpace() throws InvalidCoordinatesException, SpaceFullException, SpaceOccupiedException, IllegalWorkerActionException {

        Random rand = new Random();
        Board b = new Board();
        Player p1 = new Player("Lucio");
        Player p2 = new Player("Asdrogonio");
        Player p3 = new Player("Timburlaldo");
        b.addWorker(p1.getWorker(0));
        b.addWorker(p1.getWorker(1));
        b.addWorker(p2.getWorker(0));
        b.addWorker(p2.getWorker(1));
        b.addWorker(p3.getWorker(0));
        b.addWorker(p3.getWorker(1));

        //initialize workers
        b.initializeWorker(p1.getWorker(0), new Coord(0, 0));
        b.initializeWorker(p1.getWorker(1), new Coord(1, 0));
        b.initializeWorker(p2.getWorker(0), new Coord(0, 1));
        b.initializeWorker(p2.getWorker(1), new Coord(1, 1));
        b.initializeWorker(p3.getWorker(0), new Coord(2, 0));
        b.initializeWorker(p3.getWorker(1), new Coord(2, 1));

        for (int j = 0; j < 10000; j++) {
            //pick a random worker
            int i = rand.nextInt(6);
            Worker randomWorker = b.getAllWorkers()[i];
            List<Coord> moves = b.getMovableSpacesAround(randomWorker.getPosition(), 1);

            //if worker cannot move, skip to another worker
            if(moves.size() == 0){
                continue;
            }

            Coord move = moves.get(rand.nextInt(moves.size()));
            b.workerMove(randomWorker, move);
        }

        //no exceptions expected in this test
        Assert.assertTrue(true);
    }

    //Test exception is thrown when building on an occupied space
    @Test (expected = SpaceOccupiedException.class )
    public void notBuildOnOccupiedSpace() throws InvalidCoordinatesException, SpaceFullException, SpaceOccupiedException, IllegalWorkerActionException {

        Board b = new Board();
        Player p1 = new Player("Lucio");
        Player p2 = new Player("Asdrogonio");
        Player p3 = new Player("MamboLosco");
        b.addWorker(p1.getWorker(0));
        b.addWorker(p1.getWorker(1));
        b.addWorker(p2.getWorker(0));
        b.addWorker(p2.getWorker(1));
        b.addWorker(p3.getWorker(0));
        b.addWorker(p3.getWorker(1));

        //initialize workers
        b.initializeWorker(p1.getWorker(0), new Coord(0, 0));
        b.initializeWorker(p1.getWorker(1), new Coord(1, 0));
        b.initializeWorker(p2.getWorker(0), new Coord(0, 1));
        b.initializeWorker(p2.getWorker(1), new Coord(1, 1));
        b.initializeWorker(p3.getWorker(0), new Coord(2, 0));
        b.initializeWorker(p3.getWorker(1), new Coord(2, 1));

        //forza costruzione di un worker dove si trova un altro worker
        Worker worker1 = p1.getWorker(0);
        Coord otherWorkerPosition = p2.getWorker(0).getPosition();
        b.workerBuild(worker1, otherWorkerPosition);
    }

    //Test Board.getBuildableSpaceAround method does not give occupied (there is a worker) or full (there is a Dome) space coordinates
    //No exceptions expected from this test
    @Test
    public void notBuildOnOccupiedOrFullSpace() throws InvalidCoordinatesException, SpaceFullException, SpaceOccupiedException, IllegalWorkerActionException{

        Random rand = new Random();
        Board b = new Board();
        Player p1 = new Player("Lucio");
        Player p2 = new Player("Asdrogonio");
        Player p3 = new Player("MamboLosco");
        b.addWorker(p1.getWorker(0));
        b.addWorker(p1.getWorker(1));
        b.addWorker(p2.getWorker(0));
        b.addWorker(p2.getWorker(1));
        b.addWorker(p3.getWorker(0));
        b.addWorker(p3.getWorker(1));

        //initialize workers
        b.initializeWorker(p1.getWorker(0), new Coord(0, 0));
        b.initializeWorker(p1.getWorker(1), new Coord(1, 0));
        b.initializeWorker(p2.getWorker(0), new Coord(0, 1));
        b.initializeWorker(p2.getWorker(1), new Coord(1, 1));
        b.initializeWorker(p3.getWorker(0), new Coord(2, 0));
        b.initializeWorker(p3.getWorker(1), new Coord(2, 1));

        for (int j = 0; j < 150; j++) {
            //pick a random worker
            int i = rand.nextInt(6);
            Worker randomWorker = b.getAllWorkers()[i];
            List<Coord> builds = b.getBuildableSpacesAround(randomWorker.getPosition());

            //if this worker cannot build, skip
            if(builds.size() == 0){
                continue;
            }

            Coord build = builds.get(rand.nextInt(builds.size()));
            b.workerMove(randomWorker, build);
        }

        //no exceptions expected in this test
        Assert.assertTrue(true);
    }

    //Test exception is thrown when moving a worker on a space with Dome
    @Test (expected = SpaceFullException.class )
    public void notWorkerOnDome() throws InvalidCoordinatesException, SpaceOccupiedException, SpaceFullException, IllegalWorkerActionException {
        Random rand = new Random();
        Board b = new Board();
        Player p1 = new Player("Lucio");
        Player p2 = new Player("Asdrogonio");
        Player p3 = new Player("Timburlaldo");
        b.addWorker(p1.getWorker(0));
        b.addWorker(p1.getWorker(1));
        b.addWorker(p2.getWorker(0));
        b.addWorker(p2.getWorker(1));
        b.addWorker(p3.getWorker(0));
        b.addWorker(p3.getWorker(1));

        //initialize workers
        b.initializeWorker(p1.getWorker(0), new Coord(0, 0));
        b.initializeWorker(p1.getWorker(1), new Coord(1, 0));
        b.initializeWorker(p2.getWorker(0), new Coord(4, 4));
        b.initializeWorker(p2.getWorker(1), new Coord(1, 1));
        b.initializeWorker(p3.getWorker(0), new Coord(2, 0));
        b.initializeWorker(p3.getWorker(1), new Coord(2, 1));

        Coord dome = new Coord(0, 1);
        b.workerBuild(p1.getWorker(0), dome); //L1
        b.workerBuild(p1.getWorker(0), dome); //L2
        b.workerBuild(p1.getWorker(0), dome); //L3
        b.workerBuild(p1.getWorker(0), dome); //DOME

        //dovrebbe lanciare eccezione
        b.workerMove(p1.getWorker(0), dome);
    }

    //Test exception is thrown when building on a space with Dome.
    @Test (expected = SpaceFullException.class )
    public void notBuildOnDome() throws InvalidCoordinatesException, SpaceOccupiedException, SpaceFullException, IllegalWorkerActionException{
        Random rand = new Random();
        Board b = new Board();
        Player p1 = new Player("Lucio");
        Player p2 = new Player("Asdrogonio");
        Player p3 = new Player("Timburlaldo");
        b.addWorker(p1.getWorker(0));
        b.addWorker(p1.getWorker(1));
        b.addWorker(p2.getWorker(0));
        b.addWorker(p2.getWorker(1));
        b.addWorker(p3.getWorker(0));
        b.addWorker(p3.getWorker(1));
        b.workerMove(p1.getWorker(0), new Coord(0, 0));
        b.workerMove(p1.getWorker(1), new Coord(2, 2));
        b.workerMove(p2.getWorker(0), new Coord(4, 4));
        b.workerMove(p2.getWorker(1), new Coord(0, 4));
        b.workerMove(p3.getWorker(0), new Coord(4, 0));
        b.workerMove(p3.getWorker(1), new Coord(1, 3));

        Coord dome = new Coord(0, 1);
        b.workerBuild(p1.getWorker(0), dome); //L1
        b.workerBuild(p1.getWorker(0), dome); //L2
        b.workerBuild(p1.getWorker(0), dome); //L3
        b.workerBuild(p1.getWorker(0), dome); //DOME

        //dovrebbe lanciare eccezione
        b.workerBuild(p1.getWorker(0), dome);
    }

    //Test exception is thrown when trying to move from more than one space of distance
    @Test (expected = IllegalWorkerActionException.class )
    public void notMoveFromDistance() throws InvalidCoordinatesException, SpaceOccupiedException, SpaceFullException, IllegalWorkerActionException {
        Board b = new Board();
        Player p1 = new Player("Lucio");
        Player p2 = new Player("Asdrogonio");
        Player p3 = new Player("Timburlaldo");
        b.addWorker(p1.getWorker(0));
        b.addWorker(p1.getWorker(1));
        b.addWorker(p2.getWorker(0));
        b.addWorker(p2.getWorker(1));
        b.addWorker(p3.getWorker(0));
        b.addWorker(p3.getWorker(1));

        //initialize workers
        b.initializeWorker(p1.getWorker(0), new Coord(0, 0));
        b.initializeWorker(p1.getWorker(1), new Coord(1, 0));
        b.initializeWorker(p2.getWorker(0), new Coord(0, 1));
        b.initializeWorker(p2.getWorker(1), new Coord(1, 1));
        b.initializeWorker(p3.getWorker(0), new Coord(2, 0));
        b.initializeWorker(p3.getWorker(1), new Coord(2, 1));

        //dovrebbe lanciare eccezione
        b.workerMove(p1.getWorker(0), new Coord(1, 2));
    }

    //Test exception is thrown when trying to build form more than one space of distance
    @Test (expected = IllegalWorkerActionException.class )
    public void notBuildFromDistance() throws InvalidCoordinatesException, SpaceOccupiedException, SpaceFullException, IllegalWorkerActionException {
        Board b = new Board();
        Player p1 = new Player("Lucio");
        Player p2 = new Player("Asdrogonio");
        Player p3 = new Player("Timburlaldo");
        b.addWorker(p1.getWorker(0));
        b.addWorker(p1.getWorker(1));
        b.addWorker(p2.getWorker(0));
        b.addWorker(p2.getWorker(1));
        b.addWorker(p3.getWorker(0));
        b.addWorker(p3.getWorker(1));

        //initialize workers
        b.initializeWorker(p1.getWorker(0), new Coord(0, 0));
        b.initializeWorker(p1.getWorker(1), new Coord(1, 0));
        b.initializeWorker(p2.getWorker(0), new Coord(0, 1));
        b.initializeWorker(p2.getWorker(1), new Coord(1, 1));
        b.initializeWorker(p3.getWorker(0), new Coord(2, 0));
        b.initializeWorker(p3.getWorker(1), new Coord(2, 1));

        //dovrebbe lanciare eccezione
        b.workerBuild(p1.getWorker(0), new Coord(1, 2));
    }
}
 */