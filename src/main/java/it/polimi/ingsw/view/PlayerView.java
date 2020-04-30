package it.polimi.ingsw.view;

import it.polimi.ingsw.listeners.PlayerViewEventListener;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.listeners.ModelEventListener;
import it.polimi.ingsw.listeners.EventSource;
import it.polimi.ingsw.listeners.Listener;

import java.io.PrintStream;
import java.util.*;

public class PlayerView implements ModelEventListener, EventSource {

    private Scanner s;
    private PrintStream outputStream;


    // stare in ascolto su queste variabile per controllare che sia stato creato il player
    private String nickname;
    private PlayerViewEventListener listener;
    private String god;


    public PlayerView(){

        this.s = new Scanner(System.in);
        this.outputStream = new PrintStream(System.out);
    }

    public String getNickname() {
        return nickname;
    }

    public void askForNickname(List<String> nicknamesInGame) {
        outputStream.println("Choose a nickname: ");
        String input = s.nextLine();

        while (nicknamesInGame.contains(input)) {
            outputStream.println("Nickname already taken, please choose another nickname: ");
            input = s.nextLine();
        }

        this.nickname = input;
        listener.onNicknameChosen(this, input);
    }

    private void askToBuild(Map<Level, List<Coord>> buildableSpaces){
        outputStream.println("Where do you want to build? ");
        boolean valid = false;

        while (!valid){
            String input = s.nextLine();
            Coord c = Coord.convertStringToCoord(input);

            for (List<Coord> list : buildableSpaces.values()){
                if (list.contains(c)) {
                    valid = true;
                    break;
                }
            }

            if(valid)
                listener.onBuildChosen(this, c);
            else
                outputStream.println("Please enter a valid coordinate");
        }
    }

    private void askToMove(List<Coord> movableSpaces){
        outputStream.println("Where do you want to build? ");
        boolean valid = false;

        while (!valid){
            String input = s.nextLine();
            Coord c = Coord.convertStringToCoord(input);

            if (movableSpaces.contains(c))
                valid = true;

            if(valid)
                listener.onMoveChosen(this, c);
            else
                outputStream.println("Please enter a valid coordinate");
        }
    }

    @Override
    public void addListener(Listener listener) {
        if (!(listener instanceof PlayerViewEventListener)) {
            throw new IllegalArgumentException("Tried to register a non-ViewEventListener to View.");
        }
        this.listener = (PlayerViewEventListener) listener;
    }

//    @Override
//    public void onAllPlayersArrived() {
//        outputStream.println("All player are connected. The game is about to start...");
//    }

    @Override
    public void onBoardChanged(Board board) {
        outputStream.println(board);
    }

    @Override
    public void onGameReady() {
        outputStream.println("Set up phase is done!");
    }

    @Override
    public void onGodsChosen(List<String> gods) {
        outputStream.println("Challenger has chosen the playable gods");
        for (String s : gods){
            outputStream.println("- " + s);
        }
    }

    @Override
    public void onPlayerAdded(String nickname, int numCurr, int numTot) {
        outputStream.println(nickname + "has joined the game. Waiting for " + (numTot-numCurr) + " more player(s)");
    }

    @Override
    public void onGodSelection(List<String> gods) {


        boolean correct = false;

        while (!correct){
            outputStream.println("Choose a God, you can use \"[god_name] help\" to read the power of the God: ");
            for (String g: gods) {
                outputStream.println("- " + g);
            }

            String input = s.nextLine();

//            if (input.substring(input.length() - 4).toLowerCase().equals("help")){
//                // model needs to handle wrong deities names
//                String desc = model.getDescriptionByGodName(input.substring(0, input.length() - 5));
//                outputStream.println(desc);
//            } DA METTERE LATO CLIENT NON REMOTE VIEW

            for(String g : gods){
                if (input.toLowerCase().equals(g.toLowerCase())){
                    correct = true;
                    break;
                }
            }

            if (correct){
                try {
                    listener.onGodChosen(this, input);
                    this.god = input;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onGodsSelection(List<String> gods, int numPlayers) {
        // non dovrebbe mai arrivare al player normale
        outputStream.println("Challenger is choosing the playable gods");
    }

    @Override
    public void onStartPlayerSelection(List<String> players) {
        outputStream.println("Challenger is choosing the starting player");
    }

    @Override
    public void onMyInitialization(List<Coord> freeSpaces) {
        int i = 0;
        while (i<2) {
            if (i == 0)
                outputStream.println("Where do you want to place the first worker?");
            if (i == 1)
                outputStream.println("Where do you want to place the second worker?");
            String input  = s.nextLine();
            Coord c = Coord.convertStringToCoord(input);

            for (Coord space : freeSpaces){
                if (c.equals(space)){
                    try{
                        listener.onWorkerInitialization(this, c);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
        }
    }

    @Override
    public void onMyTurn(List<Coord> selectableWorkers) {

        boolean correct = false;

        while (!correct){
            outputStream.println("Please select the worker to use in this turn, using its position: ");
            String input = s.nextLine();
            Coord c = Coord.convertStringToCoord(input);

            for(Coord selectable : selectableWorkers){
                if (selectable.equals(c)){
                    correct = true;
                    break;
                }
            }

            if (correct){
                try{
                    listener.onWorkerChosen(this, c);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onMyAction(List<Coord> movableSpaces, Map<Level, List<Coord>> buildableSpaces, boolean canEndTurn) {

        String selectable = "\u001B[97m";
        String nonSelectable = "\u001B[90m";
        String reset = "\u001B[0m";
        boolean[] valid = new boolean[3];
        boolean correctInput = false;
        boolean isBuild = false;

        // Menu

        outputStream.println("What would you like to do?");

        if (movableSpaces.isEmpty())
            outputStream.println(nonSelectable + "1. Move" + reset);
        else {
            outputStream.println(selectable + "1. Move" + reset);
            valid[0] = true;
        }

        for (List<Coord> list : buildableSpaces.values()){
            if (!list.isEmpty()) {
                isBuild = true;
                break;
            }
        }

        if (!isBuild)
            outputStream.println(nonSelectable + "2. Build" + reset);
        else {
            outputStream.println(selectable + "2. Build" + reset);
            valid[1] = true;
        }

        if (!canEndTurn)
            outputStream.println(nonSelectable + "3. End" + reset);
        else {
            outputStream.println(selectable + "3. End" + reset);
            valid[2] = true;
        }

        String input;

        while (!correctInput){
            input = s.nextLine();

            if ((input.toLowerCase().equals("move") || input.equals("1")) && valid[0]) {
                correctInput = true;
                this.askToMove(movableSpaces);
            }
            if ((input.toLowerCase().equals("build") || input.equals("2")) && valid[1]) {
                correctInput = true;
                this.askToBuild(buildableSpaces);
            }
            if ((input.toLowerCase().equals("end") || input.equals("3")) && valid[2]) {
                correctInput = true;
                listener.skipAction(this);
            } else
                outputStream.println("Please enter a valid action");

        }

        //End Menu
    }
}