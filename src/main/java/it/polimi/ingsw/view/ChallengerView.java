package it.polimi.ingsw.view;

import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Worker;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChallengerView implements ModelEventListener, EventSource {

    private GameModel model;
    private Scanner s;
    private PrintWriter outputStream;
    private ChallengerViewEventListener listener;


    // Delego a una view generica i metodi comuni a tutti i player
    private PlayerView view;

    public ChallengerView(GameModel model){
        this.view = new PlayerView(model);
        this.s = new Scanner(System.in);
        this.outputStream = new PrintWriter(System.out);
    }

    public void chooseNumberOfPlayers(){
        int n = 0;

        while(n<1 || n>2){
            outputStream.println("Chose the number of opponents (1 or 2): ");
            n = s.nextInt();
        }

        listener.onNumberOfPlayersChosen(this, n+1);
    }

    public void chooseStartingPlayer(){
        outputStream.println("Who do you want to be the starting player? ");

        List<String> players = this.model.requestPlayersNicknames();
        for(String name: players){
            outputStream.println(name);
        }

        String choose = s.next();
        while(!players.contains(choose)){
            outputStream.println("Player name may be incorrect, please try again.");
            choose = s.next();
        }

        listener.onStartPlayerChosen(this, choose);
    }

    public void chooseGods(){
        System.out.println("The challenger chooses the gods: ");

        int numPlayers = this.model.getQueueState();
        List<String> gods = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            gods.add(s.next());
        }

        listener.onGodsChosen(this, gods);
    }

    public void askForNickname(){
        view.askForNickname();
    }

    @Override
    public void onAllPlayersArrived() {
        view.onAllPlayersArrived();
    }

    @Override
    public void onBoardChanged() {
        view.onBoardChanged();
    }

    @Override
    public void onGameReady() {
        view.onGameReady();
    }

    @Override
    public void onGodsChosen() {
        view.onGodsChosen();
    }

    @Override
    public void onPlayerAdded(String nickname) {
        view.onPlayerAdded(nickname);
    }

    @Override
    public void onGodSelection() {
        this.chooseGods();
        view.onGodSelection();
    }

    @Override
    public void onStartPlayerSelection() {
        this.chooseStartingPlayer();
        view.onStartPlayerSelection();
    }

    @Override
    public void onMyInitialization() {
        view.onMyInitialization();
    }

    @Override
    public void onMyTurn(List<Worker> selectableWorkers) {
        view.onMyTurn(selectableWorkers);
    }

    @Override
    public void onMyAction(List<Coord> movableSpaces, List<Coord> buildableSpaces) {
        view.onMyAction(movableSpaces, buildableSpaces);
    }

    public String getNickname() {
        return view.getNickname();
    }

    @Override
    public void addListener(Listener listener) {
        if (!(listener instanceof ChallengerViewEventListener)) {
            throw new IllegalArgumentException("Tried to register a non-ChallengerViewEventListener " +
                    "to ChallengerView.");
        }

        this.listener = (ChallengerViewEventListener) listener;
    }
}
