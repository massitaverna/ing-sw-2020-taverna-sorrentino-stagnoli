package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.MassiProvaCoseView;
import it.polimi.ingsw.view.View;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

//TODO: Map a view on a player (security check)
public class Controller /*implements Observer*/ implements PropertyChangeListener {

    //private Player challenger;
    private GameModel model;


    public Controller(int numPlayers){
        model = new GameModel(numPlayers);
    }

    /*
    public void setNumPlayers(int numPlayers, Player invoker) { // invoked by Challenger
        if (!(invoker.equals(model.getChallenger()))) {
            throw new IllegalAccessException("Player is invoking challenger's methods, but he is not challenger.")
        }
        model.setNumPlayers(numPlayers);
    }
    */

    /*
    public void setChallenger(Player challenger) {
        model.setChallenger(challenger);
    }
    */

    public void addNewPlayer(Player p) {
        model.addNewPlayer(p); // The first player is set in the model as Challenger
    }

    //TODO: Le view accedono al modello e vedono se sono la View Challenger
    // Se si, invoca i metodi marcati come "invoked by Challenger"

    public void setGods(List<God> gods, Player invoker) throws IllegalAccessException { // invoked by Challenger
        if (!(invoker.equals(model.getChallenger()))) {
            throw new IllegalAccessException("Player is invoking challenger's methods, but he is not challenger.")
        }
        model.setGods(gods);
        model.nextPlayer();
        // currentPlayer viene settato a Challenger+1
        // l'ordine della queue non deve essere modificato fino a che non sono stati assegnati tutti i Gods

    }

    public void setStartPlayer(Player p, Player invoker) throws IllegalAccessException { // invoked by Challenger
        if (!(invoker.equals(model.getChallenger()))) {
            throw new IllegalAccessException("Player is invoking challenger's methods, but he is not challenger.");
        }
        model.setStartPlayer(p);
        // Deve ordinare la lista dei player nel model
    }

    //NOTA: L'ordine per assegnare i Gods parte dal giocatore dopo Challenger
    //      L'ordine per posizionare gli operai parte dallo startPlayer
    //TODO: Correct handling of exceptions
    public void assignGodToPlayer(Player p, God g) throws IllegalAccessException {
        Player curr = model.getCurrentPlayer();
        if (!(p.equals(curr))) {
            throw new IllegalAccessException("Player is trying to setup not in his turn");
        }
        model.assignGodToPlayer(p, g); // Should throw IllegalArgumentException, but no warnings...
        if (!(curr.equals(model.getChallenger()))) {
            model.nextPlayer();
        }
        //Deve controllare che il god scelto sia tra i 3 selezionabili
    }

    public void initializeWorker(Player p, int x, int y) throws IllegalAccessException {
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //Unpack event
        //Invoke correct model's methods
        if (evt.getPropertyName().equals("nickname")) {
            String nickname = (String) evt.getNewValue();
            addNewPlayer(new Player(nickname));
            return;
        }

        if (evt.getPropertyName().equals("startPlayer")) {
            Player startPlayer = (Player) evt.getNewValue();
            Player invoker = ((MassiProvaCoseView) evt.getSource()).getPlayer();
            try {
                setStartPlayer(startPlayer, invoker);
            }

            catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
                System.out.println(("This means no player has been set as startPlayer."));
            }
            return;
        }

        if (evt.getPropertyName().equals("godsList")) {
            List<God> godsList = (List<God>) evt.getNewValue();
            Player invoker = ((MassiProvaCoseView) evt.getSource()).getPlayer();
            try {
                setGods(godsList, invoker);
            }
            catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
                System.out.println(("This means no gods have been set for this game."));
            }
            return;
        }

        if (evt.getPropertyName().equals("god")) {
            God god = (God) evt.getNewValue();
            Player invoker = ((MassiProvaCoseView) evt.getSource()).getPlayer();
            try {
                assignGodToPlayer(invoker, god);
            }
            catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            return;
        }

        if (false || true) {}// FARE PROSSIMI CASI
        throw new IllegalArgumentException("Controller received an unknown event.");
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
