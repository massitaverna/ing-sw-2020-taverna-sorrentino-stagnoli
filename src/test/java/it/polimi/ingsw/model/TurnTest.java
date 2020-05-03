package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.handler.RequestHandler;
import it.polimi.ingsw.model.handler.RequestHandlerCreator;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class TurnTest {
    @Test
    public void forcesAreInMovableSpaces() {

        GameModel model = new GameModel();
        model.addNewPlayer(new Player("Pippo"));
        model.addNewPlayer(new Player("Pluto"));
        model.addNewPlayer(new Player("Paperino"));

        List<String> availableGods = (new GameModel()).getAvailableGods().stream()
                .map(God::getName).collect(Collectors.toList());


        //Boh...



        for (String god : availableGods) {
            RequestHandler rh = new RequestHandlerCreator(god).createHandler();
            //rh.getValidSpaces();

        }
    }
}
