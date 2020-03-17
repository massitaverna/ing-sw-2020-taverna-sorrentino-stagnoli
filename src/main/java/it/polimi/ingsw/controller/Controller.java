package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

//TODO: Map a view on a player (security check)
public class Controller implements Observer {

    private Player challenger;
    private Model model;
    private View view;

    public Controller(){
        model = new Model();
        view = new View();
    }

    //TODO: Check that the methods "invoked by Challenger" are actually invoked by him
    public void setNumPlayers(int numPlayers) { // invoked by Challenger
        model.setNumPlayers(numPlayers);
    }

    //The following method is not used

    /*public void setChallenger(Player challenger) {
        model.setChallenger(challenger);
    }*/

    public void addNewPlayer(Player p) {
        model.addNewPlayer(p); // The first player is set in the model as Challenger
    }

    //TODO: Le view accedono al modello e vedono se sono la View Challenger
    // Se si, invoca i metodi marcati come "invoked by Challenger"
    public void setStartPlayer(Player p) { // invoked by Challenger
        model.setStartPlayer(p);
        // Deve ordinare la lista dei player nel model
    }

    public void setGods(Collection<God> gods) { // invoked by Challenger
        model.setGods(gods);
        // Da qui, il model ha un currentPlayer
    }

    //TODO: Correct handling of exception
    public void assignGodToPlayer(Player p, God g) throws IllegalAccessException {
        Player curr = model.getCurrentPlayer();
        if (!(p.equals(curr))) {
            throw new IllegalAccessException("Player is trying to setup not in his turn");
        }
        model.assignGodToPlayer(p, g);
        //Deve controllare che il god scelto sia tra i 3 selezionabili
    }

    public void initializeWorkers(Player p, Worker)
//Setup scheme:
        //  caso1: setto challenger e numPlayers
        //  caso2: aggiungo player/s
        //  caso3: sono tutti collegati -> setUp()

        //  setUp():
        //      caso 3a: riceve le divinità scelte e lo startPlayer
        //               le setta nel model
        //      caso 3b: for(Player p : queue) ricevi le scelte dei player
}
