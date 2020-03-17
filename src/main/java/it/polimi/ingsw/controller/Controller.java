package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {

    private Player challenger;
    private Model model;
    private View view;

    public Controller(){
        model = new Model();
        view = new View();
    }

    @Override
    public void update(Observable o, Object arg) {
        //  caso1: setto challenger e numPlayers
        //  caso2: aggiungo player/s
        //  caso3: sono tutti collegati -> setUp()

        //  setUp():
        //      riceve le divinità scelte e lo startPlayer
        //      le setta nel model
        //      for(Player p : queue) ricevi le scelte dei player
    }
}
