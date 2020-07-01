package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.view.RemotePlayerView;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


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

    }

    @Test
    public void setupTest() {
        numPlayers();
        addPlayers();
        chooseGods();
        chooseGod();
    }

    private void numPlayers() {
        controller.onNumberOfPlayersChosen(view1,3);
        assertEquals(3, model.getNumPlayers());
    }

    private void addPlayers() {
        controller.onNicknameChosen(view1, "A");
        controller.onNicknameChosen(view2, "B");
        controller.onNicknameChosen(view3, "C");
    }

    private void chooseGods() {
        List<String> gods = Arrays.asList("Apollo", "Artemis", "Atlas");
        controller.onGodsChosen(view1, gods);
    }

    private void chooseGod() {
        List<String> gods = Arrays.asList("Apollo", "Artemis", "Atlas");
        gods = new ArrayList<>(gods);
        controller.onGodChosen(view2, gods.remove(0));
        controller.onGodChosen(view3, gods.remove(0));
        controller.onGodChosen(view1, gods.remove(0));

    }
}
