package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.view.RemotePlayerView;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControllerTest {
    private static GameModel model;
    private static Controller controller;
    private static RemotePlayerView view1;
    private static RemotePlayerView view2;
    private static RemotePlayerView view3;


    @BeforeClass
    public static void before() throws FileNotFoundException {
        model = new GameModel();
        controller = new Controller(model);
        view1 = new RemotePlayerView("A", new Connection(null,null,null));
        view2 = new RemotePlayerView("B", new Connection(null,null,null));
        view3 = new RemotePlayerView("C", new Connection(null,null,null));
        model.addListener(view1);
        model.addListener(view2);
        model.addListener(view3);
    }

    @Test
    public void setupTest() {
        numPlayers();
        addPlayers();
        chooseGods();
        chooseGod();
        startPlayer();
        workerInit();
        chooseWorker();
        move();
        build();
        skip();
    }

    private void numPlayers() {
        controller.onNumberOfPlayersChosen(view1,3);
        assertEquals(3, model.getNumPlayers());
    }

    private void addPlayers() {
        controller.onNicknameChosen(view1, view1.getNickname());
        controller.onNicknameChosen(view2, view2.getNickname());
        controller.onNicknameChosen(view3, view3.getNickname());
    }

    private void chooseGods() {
        List<String> gods = Arrays.asList("Apollo", "Atlas", "Demeter");
        gods = new ArrayList<>(gods);
        controller.onGodsChosen(view1, gods);
    }

    private void chooseGod() {
        List<String> gods = Arrays.asList("Apollo", "Atlas", "Demeter");
        gods = new ArrayList<>(gods);
        controller.onGodChosen(view2, gods.remove(0));
        controller.onGodChosen(view3, gods.remove(0));
        controller.onGodChosen(view1, gods.remove(0));
        assertEquals(model.getPlayerByNickname(view2.getNickname()).getGod().getName(), "Apollo");
        assertEquals(model.getPlayerByNickname(view3.getNickname()).getGod().getName(), "Atlas");
        assertEquals(model.getPlayerByNickname(view1.getNickname()).getGod().getName(), "Demeter");
    }

    private void startPlayer() {
        controller.onStartPlayerChosen(view1, view1.getNickname());
    }

    private void workerInit() {
        controller.onWorkerInitialization(view1, new Coord(0, 0));
        controller.onWorkerInitialization(view1, new Coord(0, 1));
        controller.onWorkerInitialization(view2, new Coord(3, 1));
        controller.onWorkerInitialization(view2, new Coord(3, 2));
        controller.onWorkerInitialization(view3, new Coord(2, 0));
        controller.onWorkerInitialization(view3, new Coord(2, 1));
    }

    private void chooseWorker() {
        controller.onWorkerChosen(view1, new Coord(0, 0));
    }

    private void move() {
        controller.onMoveChosen(view1, new Coord(1, 0));
    }

    private void build() {
        controller.onBuildChosen(view1, new Coord(0, 0), Level.LVL1);
    }

    private void skip() {
        controller.skipAction(view1);
    }
}
