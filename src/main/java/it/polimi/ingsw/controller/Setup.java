package it.polimi.ingsw.controller;

import it.polimi.ingsw.listeners.EventSource;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.god.God;
import it.polimi.ingsw.listeners.ViewEventListener;
import it.polimi.ingsw.listeners.ChallengerViewEventListener;
import it.polimi.ingsw.model.god.GodCreator;
import it.polimi.ingsw.view.MassiProvaCoseView;
import it.polimi.ingsw.view.View;

import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.stream.Collectors;

//TODO: Map a view on a player (security check)
public class Setup /*implements ViewEventListener, ChallengerViewEventListener*/ {

    //private Player challenger;
    private GameModel model;


    public Setup(GameModel model){
        this.model = model;
    }


    public void setNumPlayers(int numPlayers) { // invoked by Challenger
        /*if (!(invoker.equals(model.getChallenger()))) {
            throw new IllegalAccessException("Player is invoking " +
                    "challenger's methods, but he is not challenger.")
        }*/
        model.setNumPlayers(numPlayers);
    }

    /*
    public void setChallenger(Player challenger) {
        model.setChallenger(challenger);
    }
    */

    public void addNewPlayer(Player p) {
        model.addNewPlayer(p); // The first player is set in the model as Challenger
    }

    public void setPlayerColor(Player p, Color c) {
        try {
            model.setPlayerColor(p, c);
        }
        catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
        // Handle re-asking for color
    }
    //TODO: Le view accedono al modello e vedono se sono la View Challenger
    // Se si, invoca i metodi marcati come "invoked by Challenger"

    public void setGods(List<God> gods) { // invoked by Challenger
        /*if (!(invoker.equals(model.getChallenger()))) {
            throw new IllegalAccessException("Player is invoking " +
                    "challenger's methods, but he is not challenger.");
        }*/
        model.setGods(gods);
        model.nextPlayer();
        // currentPlayer viene settato a Challenger+1
        // l'ordine della queue non deve essere modificato fino a che non sono stati assegnati tutti i Gods

    }

    public void setStartPlayer(Player p) { // invoked by Challenger
        /*if (!(invoker.equals(model.getChallenger()))) {
            throw new IllegalAccessException("Player is invoking " +
                    "challenger's methods, but he is not challenger.");
        }*/
        model.setStartPlayer(p);
        // Deve ordinare la lista dei player nel model
    }

    //NOTA: L'ordine per assegnare i Gods parte dal giocatore dopo Challenger
    //      L'ordine per posizionare gli operai parte dallo startPlayer
    //TODO: Correct handling of exceptions
    public void assignGodToPlayer(Player p, God g) throws IllegalArgumentException,
            IllegalStateException {

        Player curr = model.getCurrentPlayer();
        if (!(p.equals(curr))) {
            throw new IllegalStateException("Player is trying to setup not in his turn.");
        }

        if (g == null) {
            throw new IllegalArgumentException("Chosen god has been previously chosen by another " +
                    "player or has never been selected by Challenger.");
        }

        model.assignGodToPlayer(p, g); // Should throw IllegalArgumentException, but no warnings...
        if (!(curr.equals(model.getChallenger()))) {
            model.nextPlayer();
        }
    }

    public void initializeWorker(Player player, Coord place) throws IllegalStateException {
        Player curr = model.getCurrentPlayer();
        if (!(player.equals(curr))) {
            throw new IllegalStateException("Player is trying to setup not in his turn.");
        }
        Worker toBeInitialized = player.getWorkersList().stream()
                .filter(worker -> worker.getPosition() == null).findFirst().orElse(null);
        if (toBeInitialized == null) {
            throw new IllegalStateException("Workers have already been initialized for this player.");
        }
        // The above logic is working as long as:
        // 1. every player has no null in his workersList and
        // 2. every worker has always NotNull coordinates after initialization
        model.initializeWorker(toBeInitialized, place);
        // Deve aggiornare il currentPlayer se sono stati settati 2 lavoratori
    }


    //---------------------------------------------------------------------------------

    /*
    EVENT HANDLING SECTION
        The following methods are called by the same-name methods in wrapper Controller.
        They actually implement interface methods on behalf of Controller.
     */

    public void onNicknameChosen(EventSource source, String nickname) {
        addNewPlayer(new Player(nickname));
    }

    public void onColorChosen(EventSource source, Color color) {
        String nickname = ((View) source).getNickname();
        Player player = model.getPlayerByNickname(nickname);
        setPlayerColor(player, color);
    }

    public void onNumberOfPlayersChosen(EventSource source, int num) {
        setNumPlayers(num);
    }

    public void onGodsChosen(EventSource source, List<String> godsNames) {
        List<God> gods = new ArrayList<God>();
        for (String s : godsNames) {
            God god = new GodCreator().createGod(s);
            gods.add(god);
        }
        setGods(gods);
    }

    public void onGodChosen(EventSource source, String godName) {
        //TODO: check that the player has not chosen his god yet
        List<God> gods = model.getGods();
        God chosenGod = gods.stream().filter(god -> god.getName().equals(godName))
                .findFirst().orElse(null);

        String nickname = ((View) source).getNickname();
        Player player = model.getPlayerByNickname(nickname);
        try {
            assignGodToPlayer(player, chosenGod);
        }
        catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            System.out.println("There may be something wrong in turn rotation handling");
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("This means no god has been assigned to the player.");
            //Il turno deve rimanere al player che ha fatto sollevare l'eccezione
        }
    }

    public void onStartPlayerChosen(EventSource source, String startPlayerNickname) {
        Player startPlayer = model.getPlayerByNickname(startPlayerNickname);
        setStartPlayer(startPlayer);
    }

    public void onWorkerInitialized(EventSource source, int whichWorker, int x, int y) {
        //TODO: check that the player has not initialized both workers yet
        Coord coord = new Coord(x, y);
        String nickname = ((View) source).getNickname();
        Player player = model.getPlayerByNickname(nickname);
        try {
            initializeWorker(player, coord);
        }

        catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            System.out.println("There may be something wrong in turn rotation handling");
        }
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
