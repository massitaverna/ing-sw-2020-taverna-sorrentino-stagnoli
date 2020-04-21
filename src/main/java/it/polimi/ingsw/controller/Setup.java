package it.polimi.ingsw.controller;

import it.polimi.ingsw.listeners.EventSource;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.god.God;
import it.polimi.ingsw.model.god.GodCreator;
import it.polimi.ingsw.view.PlayerView;

import java.util.*;

//TODO: Map a view on a player (security check)
public class Setup {

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

    public void addNewPlayer(Player p) {
        model.addNewPlayer(p); // The first player should be the Challenger
        if (model.allPlayersArrived()) {
            //model.startSetup();
        }
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

    public void setGods(List<God> gods) { // invoked by Challenger
        /*if (!(invoker.equals(model.getChallenger()))) {
            throw new IllegalAccessException("Player is invoking " +
                    "challenger's methods, but he is not challenger.");
        }*/
        model.setGods(gods);
        model.nextPlayer();
        //model.askForGodChoice();
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

        if (p == null) {
            throw new IllegalArgumentException("Cannot assign a god to a Null player.");
        }

        Player curr = model.getCurrentPlayer();
        if (!(p.equals(curr))) {
            throw new IllegalStateException("Player is trying to setup not in his turn.");
        }

        if (g == null) {
            throw new IllegalArgumentException("Chosen god has been previously chosen by another " +
                    "player or has never been selected by Challenger.");
        }

        model.assignGodToPlayer(p, g); // Should throw IllegalArgumentException, but no warnings...
        model.nextPlayer();
        //model.askForGodChoice();

    }

    public void initializeWorker(Player player, Coord place) throws IllegalStateException {
        if (player == null) {
            throw new IllegalArgumentException("Cannot initialize workers for a Null player.");
        }

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
        String nickname = ((PlayerView) source).getNickname();
        try {
            Player player = model.getPlayerByNickname(nickname);
            setPlayerColor(player, color);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("View's and Model's nicknames mismatch.");
        }
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

        String nickname = ((PlayerView) source).getNickname();
        Player player;
        try {
            player = model.getPlayerByNickname(nickname);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("View's and Model's nicknames mismatch.");
            player = null;
        }
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
        try {
            Player startPlayer = model.getPlayerByNickname(startPlayerNickname);
            setStartPlayer(startPlayer);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.print("This means no player has been set as startPlayer.");
        }
    }

    public void onWorkerInitialized(EventSource source, int x, int y) {
        //TODO: check that the player has not initialized both workers yet
        Coord coord = new Coord(x, y);
        String nickname = ((PlayerView) source).getNickname();
        Player player;
        try {
            player = model.getPlayerByNickname(nickname);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            player = null;
        }
        try {
            initializeWorker(player, coord);
        }

        catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            System.out.println("There may be something wrong in turn rotation handling");
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("This means the worker has not been initialized for the player.");
        }
    }


//Setup scheme:
        //  caso2: aggiungo player/s
        //  caso3: sono tutti collegati -> setUp()

        //  setUp():
        //      caso 3a: riceve le divinità scelte e lo startPlayer
        //               le setta nel model
        //      caso 3b: for(Player p : queue) ricevi le scelte dei player
}
