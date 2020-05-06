package it.polimi.ingsw;

import it.polimi.ingsw.controller.RealController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.view.ChallengerView;
import it.polimi.ingsw.view.PlayerView;


import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        boolean simulation = false;



        GameModel model = new GameModel();
        RealController controller = new RealController(model);
        ChallengerView view1 = new ChallengerView();
        PlayerView view2 = new PlayerView();
        PlayerView view3 = new PlayerView();

        //-----------------SIMULATION------------------
        if (simulation) {
            Scanner sc = new Scanner(System.in);
            view1 = new ChallengerView(sc);
            view2 = new PlayerView(sc);
            view3 = new PlayerView(sc);
        }
        //-----------------SIMULATION------------------
        model.addListener(view1);
        model.addListener(view2);
        model.addListener(view3);
        view1.addListener(controller);
        view2.addListener(controller);
        view3.addListener(controller);

        view1.chooseNumberOfPlayers();
        // Passing just an empty list
        //TODO: pass correct list somehow
        view1.askForNickname(new ArrayList<>());
        view2.askForNickname(new ArrayList<>());
        view3.askForNickname(new ArrayList<>());

    }
}
