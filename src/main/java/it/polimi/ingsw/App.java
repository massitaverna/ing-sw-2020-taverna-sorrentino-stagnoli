package it.polimi.ingsw;

import it.polimi.ingsw.controller.RealController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.view.ChallengerView;
import it.polimi.ingsw.view.PlayerView;

import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        GameModel model = new GameModel();
        RealController controller = new RealController(model);
        ChallengerView view1 = new ChallengerView();
        PlayerView view2 = new PlayerView();
        PlayerView view3 = new PlayerView();
        model.addListener(view1);
        model.addListener(view2);
        model.addListener(view3);
        view1.addListener(controller);
        view2.addListener(controller);
        view3.addListener(controller);

        view1.chooseNumberOfPlayers();
        // Passing just an empty list
        //TODO: pass correct list somehow
        view2.askForNickname(new ArrayList<>());
        view3.askForNickname(new ArrayList<>());

    }
}
