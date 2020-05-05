package it.polimi.ingsw.view;

import it.polimi.ingsw.listeners.*;
import it.polimi.ingsw.model.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ChallengerView implements ModelEventListener, EventSource, Runnable {

    private Scanner s;
    private PrintStream outputStream;
    private ChallengerViewEventListener listener;


    // Delego a una view generica i metodi comuni a tutti i player
    private PlayerView view;

    public ChallengerView(){
        this.view = new PlayerView();
        this.s = new Scanner(System.in);
        this.outputStream = new MyPrintStream(System.out);
    }

    public ChallengerView(Scanner s) {
        this.s = s;
        this.view = new PlayerView(s);
        this.outputStream = new MyPrintStream(System.out);
    }

    // Classe provvisoria per testing
    class MyPrintStream extends PrintStream {
        public MyPrintStream(OutputStream outputStream) {
            super(outputStream);
        }
        public void println(Object o) {
            super.println(view.getNickname() + "'s VIEW");
            super.println(o);
        }
        public void println(String s) {
            super.println(view.getNickname() + "'s VIEW");
            super.println(s);
        }
    }

    public void run() {
        chooseNumberOfPlayers();
    }

    public void chooseNumberOfPlayers() {
        int n = 0;

        while(n<1 || n>2){
            outputStream.println("Chose the number of opponents (1 or 2): ");
            try{
                n = s.nextInt();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        listener.onNumberOfPlayersChosen(this, n+1);
    }

    public void askForNickname(List<String> nicknamesInGame){
        view.askForNickname(nicknamesInGame);
    }

//    @Override
//    public void onAllPlayersArrived() {
//        view.onAllPlayersArrived();
//    }

    @Override
    public void onBoardChanged(Board board) {
        view.onBoardChanged(board);
    }

    @Override
    public void onGameReady() {
        view.onGameReady();
    }

    @Override
    public void onGodsChosen(List<String> gods) {
        view.onGodsChosen(gods);
    }

    @Override
    public void onPlayerAdded(String nickname, int numCurr, int numTot) {
        view.onPlayerAdded(nickname, numCurr, numTot);
    }

    @Override
    public void onGodSelection(List<String> gods) {
        view.onGodSelection(gods);
    }

    @Override
    public void onGodsSelection(List<String> gods, int numPlayers) {
        outputStream.println("Choose the gods to use in this game: ");
        boolean valid = false;
        boolean godIsOk = false;
        List<String> choices = new ArrayList<>();

        while (!valid){
            String input = s.nextLine();
            input = input.toLowerCase();

            for (String god : gods) {
                if (god.toLowerCase().equals(input)) {
                    choices.add(god);
                    godIsOk = true;
                    break;
                }
            }

            if (choices.size() == numPlayers){
                valid = true;
                listener.onGodsChosen(this, choices);
            } else
                outputStream.println("Choose another god");

            if(godIsOk)
                godIsOk = false;
            else
                outputStream.println("Invalid input");
        }
    }

    @Override
    public void onStartPlayerSelection(List<String> players) {
        outputStream.println("Choose the starting player: ");

        for (int i = 0; i<players.size(); i++) {
            outputStream.println( (i+1) + " " + players.get(i));
        }

        boolean valid = false;

        while (!valid){
            String input = s.nextLine();

            if (input.equals("1") || input.toLowerCase().equals(players.get(0).toLowerCase())){
                valid = true;
                listener.onStartPlayerChosen(this, players.get(0));
            } if (input.equals("2") || input.toLowerCase().equals(players.get(1).toLowerCase())){
                valid = true;
                listener.onStartPlayerChosen(this, players.get(1));
            } if (input.equals("3") || input.toLowerCase().equals(players.get(2).toLowerCase())){
                valid = true;
                listener.onStartPlayerChosen(this, players.get(2));
            } else
                outputStream.println("Invalid nickname");
        }

    }

    @Override
    public void onMyInitialization(List<Coord> freeSpaces) {
        view.onMyInitialization(freeSpaces);
    }

    @Override
    public void onMyTurn(List<Coord> selectableWorkers) {
        view.onMyTurn(selectableWorkers);
    }

    @Override
    public void onMyAction(List<Coord> movableSpaces, Map<Level, List<Coord>> buildableSpaces, boolean canEndTurn) {
        view.onMyAction(movableSpaces, buildableSpaces, canEndTurn);
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
        view.addListener((PlayerViewEventListener) listener);
    }
}
