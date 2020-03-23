package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

//TODO: Map a view on a player (security check)
public class Controller /*implements Observer*/ {

    private Player challenger;
    private GameModel model;
    private View view;

    public Controller(){
        model = new GameModel();
        view = new View();
    }

    public void setNumPlayers(int numPlayers, Player invoker) { // invoked by Challenger
        if (!(invoker.equals(model.getChallenger()))) {
            throw new IllegalAccessException("Player is invoking challenger's methods, but he is not challenger.")
        }
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
    public void setStartPlayer(Player p, Player invoker) { // invoked by Challenger
        if (!(invoker.equals(model.getChallenger()))) {
            throw new IllegalAccessException("Player is invoking challenger's methods, but he is not challenger.")
        }
        model.setStartPlayer(p);
        // Deve ordinare la lista dei player nel model
    }

    public void setGods(List<God> gods, Player invoker) { // invoked by Challenger
        if (!(invoker.equals(model.getChallenger()))) {
            throw new IllegalAccessException("Player is invoking challenger's methods, but he is not challenger.")
        }
        model.setGods(gods);
        // Da qui, il model ha un currentPlayer
    }

    //NOTA: L'ordine per assegnare i Gods parte dal giocatore dopo Challenger
    //      L'ordine per posizionare gli operai parte dallo startPlayer
    //TODO: Correct handling of exception
    public void assignGodToPlayer(Player p, God g) throws IllegalAccessException {
        Player curr = model.getCurrentPlayer();
        if (!(p.equals(curr))) {
            throw new IllegalAccessException("Player is trying to setup not in his turn");
        }
        model.assignGodToPlayer(p, g);
        //Deve controllare che il god scelto sia tra i 3 selezionabili
    }

    public void initializeWorker(Player p, int x, int y) {
        Player curr = model.getCurrentPlayer();
        if (!(p.equals(curr))) {
            throw new IllegalAccessException("Player is trying to setup not in his turn");
        }
        model.initializeWorker(p, x, y);
        // Deve controllare che sia fattibile
        // Deve aggiornare il currentWorker se sono stati settati 2 lavoratori
    }

    @Override
    public void update(Observable o, Object arg) {

    }
//Setup scheme:
        //  caso1: setto challenger e numPlayers
        //  caso2: aggiungo player/s
        //  caso3: sono tutti collegati -> setUp()

        //  setUp():
        //      caso 3a: riceve le divinità scelte e lo startPlayer
        //               le setta nel model
        //      caso 3b: for(Player p : queue) ricevi le scelte dei player
}
