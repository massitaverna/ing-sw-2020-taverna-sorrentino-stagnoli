package it.polimi.ingsw.controller;

import it.polimi.ingsw.listeners.EventSource;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.GodSelectionState;
import it.polimi.ingsw.view.RemotePlayerView;

import java.util.*;


public class Setup {

    private final GameModel model;


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
            model.changeState(new GodSelectionState(model));
            model.nextStep();
        }
    }

    public void setGods(List<String> gods) { // invoked by Challenger
        /*if (!(invoker.equals(model.getChallenger()))) {
            throw new IllegalAccessException("Player is invoking " +
                    "challenger's methods, but he is not challenger.");
        }*/
        model.setGods(gods);
        model.nextStep();
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
        model.nextStep();
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

        model.assignGodToPlayer(p, g);

        if (model.getAvailableGods().isEmpty()) {
            model.changeState(new WorkersInitState(model));
        }
        model.nextStep();


    }

    public void initializeWorker(Player player, Coord place) throws IllegalStateException {
        if (player == null) {
            throw new IllegalArgumentException("Cannot initialize workers for a Null player.");
        }

        Player curr = model.getCurrentPlayer();
        if (!(player.equals(curr))) {
            throw new IllegalStateException("Player is trying to setup not in his turn.");
        }

        boolean workersInitAlreadyDone = player.getWorkersList().stream()
                .noneMatch(worker -> worker.getPosition() == null);
        if (workersInitAlreadyDone) {
            throw new IllegalStateException("Workers have already been initialized for this player.");
        }
        // The above logic is working as long as:
        // 1. every player has no null in his workersList and
        // 2. every worker has always NotNull coordinates after initialization
        model.initializeWorker(place);

        if (model.hasNewCycleBegun() && model.getCurrentPlayer().getWorkersList().stream()
                .noneMatch(w -> w.getPosition() == null)) {
            model.changeState(new BeginState(model));
        }

        model.nextStep();
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

    /*
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
     */

    public void onNumberOfPlayersChosen(EventSource source, int num) {
        setNumPlayers(num);
    }

    public void onGodsChosen(EventSource source, List<String> godsNames) {
        setGods(godsNames);
    }

    public void onGodChosen(EventSource source, String godName) {
        //TODO: check that the player has not chosen his god yet
        List<God> gods = model.getAvailableGods();
        God chosenGod = gods.stream().filter(god -> god.getName().toLowerCase().equals(godName.toLowerCase()))
                .findFirst().orElse(null);

        String nickname = ((RemotePlayerView) source).getNickname();
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

    public void onWorkerInitialization(EventSource source, Coord coord) {
        //TODO: check that the player has not initialized both workers yet
        String nickname = ((RemotePlayerView) source).getNickname();
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

}
