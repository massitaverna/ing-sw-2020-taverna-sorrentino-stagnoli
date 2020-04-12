package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.model.IllegalWorkerActionException;
import it.polimi.ingsw.exceptions.model.InvalidCoordinatesException;
import it.polimi.ingsw.exceptions.model.SpaceFullException;
import it.polimi.ingsw.exceptions.model.SpaceOccupiedException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class BoardTest {

    //Usa solo Board.workerMove(), movimento forzato sulla posizione di un altro worker
    @Test ( expected = SpaceOccupiedException.class )
    public void notTwoWorkersOnSameSpaceV1() throws InvalidCoordinatesException, SpaceFullException, SpaceOccupiedException, IllegalWorkerActionException {

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
        b.workerMove(p1.getWorker(0), new Coord(rand.nextInt(5), rand.nextInt(5)));
        b.workerMove(p1.getWorker(1), new Coord(rand.nextInt(5), rand.nextInt(5)));
        b.workerMove(p2.getWorker(0), new Coord(rand.nextInt(5), rand.nextInt(5)));
        b.workerMove(p2.getWorker(1), new Coord(rand.nextInt(5), rand.nextInt(5)));
        b.workerMove(p3.getWorker(0), new Coord(rand.nextInt(5), rand.nextInt(5)));
        b.workerMove(p3.getWorker(1), new Coord(rand.nextInt(5), rand.nextInt(5)));

        //se non è ancora stata lanciata l'eccezione
        while(true){
            int i = rand.nextInt(6);
            Worker randomWorker = b.getAllWorkers()[i];
            Coord otherWorker = b.getAllWorkers()[(i==0?i+1:(i==5?i-1:i+1))].getPosition();
            b.workerMove(randomWorker, otherWorker);
        }
    }

    //Usa Board.GetMovableSpacesAround(..) per i movimenti
    @Test
    public void notTwoWorkersOnSameSpaceV2() throws InvalidCoordinatesException, SpaceFullException, SpaceOccupiedException, IllegalWorkerActionException {

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

        for (int j = 0; j < 10000; j++) {
            int i = rand.nextInt(6);
            Worker randomWorker = b.getAllWorkers()[i];
            List<Coord> moves = b.getMovableSpacesAround(randomWorker.getPosition(), 1);
            if(moves.size() == 0){
                continue;
            }
            Coord move = moves.get(rand.nextInt(moves.size()));
            b.workerMove(randomWorker, move);
        }

        Assert.assertTrue(true);
    }

    //Forza la costruzione sul un altro worker
    @Test (expected = SpaceFullException.class)
    public void notBuildOnOccupiedSpaceV1() throws InvalidCoordinatesException, SpaceFullException, SpaceOccupiedException, IllegalWorkerActionException {

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
        b.workerMove(p1.getWorker(0), new Coord(0, 0));
        b.workerMove(p1.getWorker(1), new Coord(2, 2));
        b.workerMove(p2.getWorker(0), new Coord(4, 4));
        b.workerMove(p2.getWorker(1), new Coord(0, 4));
        b.workerMove(p3.getWorker(0), new Coord(4, 0));
        b.workerMove(p3.getWorker(1), new Coord(1, 3));

        while(true){
            int i = rand.nextInt(6);
            Worker randomWorker = b.getAllWorkers()[i];
            Coord otherWorker = b.getAllWorkers()[(i==0?i+1:(i==5?i-1:i+1))].getPosition();
            b.workerBuild(randomWorker, otherWorker);
        }
    }

    //usa Board.getBuildableSpacesAround(...) per costruire
    @Test
    public void notBuildOnOccupiedSpaceV2() throws InvalidCoordinatesException, SpaceFullException, SpaceOccupiedException, IllegalWorkerActionException{

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
        b.workerMove(p1.getWorker(0), new Coord(0, 0));
        b.workerMove(p1.getWorker(1), new Coord(2, 2));
        b.workerMove(p2.getWorker(0), new Coord(4, 4));
        b.workerMove(p2.getWorker(1), new Coord(0, 4));
        b.workerMove(p3.getWorker(0), new Coord(4, 0));
        b.workerMove(p3.getWorker(1), new Coord(1, 3));

        for (int j = 0; j < 30; j++) {
            int i = rand.nextInt(6);
            Worker randomWorker = b.getAllWorkers()[i];
            List<Coord> builds = b.getBuildableSpacesAround(randomWorker.getPosition());
            if(builds.size() == 0){
                continue;
            }
            Coord build = builds.get(rand.nextInt(builds.size()));
            b.workerMove(randomWorker, build);
        }

        Assert.assertTrue(true);
    }

    //
    @Test (expected = SpaceFullException.class)
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
        b.workerMove(p1.getWorker(0), dome);
    }

    @Test (expected = SpaceFullException.class)
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

    @Test (expected = IllegalWorkerActionException.class)
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
        b.workerMove(p1.getWorker(0), new Coord(0, 0));
        b.workerMove(p1.getWorker(1), new Coord(2, 2));
        b.workerMove(p2.getWorker(0), new Coord(4, 4));
        b.workerMove(p2.getWorker(1), new Coord(0, 4));
        b.workerMove(p3.getWorker(0), new Coord(4, 0));
        b.workerMove(p3.getWorker(1), new Coord(1, 3));

        //dovrebbe lanciare eccezione
        b.workerMove(p1.getWorker(0), new Coord(1, 2));
    }

    @Test (expected = IllegalWorkerActionException.class)
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
        b.workerMove(p1.getWorker(0), new Coord(0, 0));
        b.workerMove(p1.getWorker(1), new Coord(2, 2));
        b.workerMove(p2.getWorker(0), new Coord(4, 4));
        b.workerMove(p2.getWorker(1), new Coord(0, 4));
        b.workerMove(p3.getWorker(0), new Coord(4, 0));
        b.workerMove(p3.getWorker(1), new Coord(1, 3));

        //dovrebbe lanciare eccezione
        b.workerBuild(p1.getWorker(0), new Coord(1, 2));
    }
}
