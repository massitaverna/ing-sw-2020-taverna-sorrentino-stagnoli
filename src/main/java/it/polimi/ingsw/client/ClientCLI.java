package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.Connection;
import jdk.jshell.Snippet;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ClientCLI {

    private PrintStream outputStream;
    private Scanner s;

    //used for reacting when a message arrives from the server
    private class MessageReceiver implements Observer<Object> {
        @Override
        public void update(Object message) {
            System.out.println("Received: " + message.toString());
            receivedObject = message;
            handleMessageReceived();
        }
    }

    private Object receivedObject;
    private Connection serverConnection;

    private String nickname;
    private boolean isChallenger;

    public ClientCLI(Connection serverConnection){
        //initialize connection object to send/receive objects from the server
        this.serverConnection = serverConnection;
        this.serverConnection.addObserver(new MessageReceiver());

        this.outputStream = new PrintStream(System.out);
        this.s = new Scanner(System.in);
    }

    private void handleMessageReceived(){
        List<Object> objs;
        if(receivedObject instanceof List)
            objs = (List<Object>) receivedObject;
        else {
            outputStream.println("Something went wrong in handling received message");
            return;
        }
        String event = (String) objs.get(0);

        switch (event) {
            case "onBoardChanged":
                Board b = (Board)objs.get(0);
                outputStream.println(b.toString());
                break;

            case "onGameReady":
                outputStream.println("Set up phase is done!");
                break;

            case "onGodsChosen":
                List<String> gods = (List<String>) objs.get(1);
                outputStream.println("Challenger has chosen the playable gods");
                for (String s : gods){
                    outputStream.println("- " + s);
                }
                break;

            case "onPlayerAdded":
                String nickname = (String) objs.get(1);
                int numCurr = (int) objs.get(2);
                int numTot = (int) objs.get(3);
                outputStream.println(nickname + " has joined the game. Waiting for " + (numTot-numCurr) + " more player(s)");
                break;

            case "onMessage":
                handleGameMessage();
                break;

            case "onGodSelection":
                List<String> godsForSelection = (List<String>) objs.get(1);
                onGodSelection(godsForSelection);
                break;

            case "onGodsSelection":
                List<String> selectedGods = (List<String>) objs.get(1);
                int numPlayers = (int) objs.get(2);
                selectedGods.forEach(g -> System.out.println("- " + g));
                break;

            case "onStartPlayerSelection":
                if(isChallenger){
                    //TODO: ask the challenger to choose starting player
                }
                else {
                    System.out.println("Challenger is choosing the starting player");
                }
                break;

            case "onMyInitialization":
                List<Coord> freeSpaces = (List<Coord>) objs.get(1);
                //TODO: ask the player to initialize workers
                break;

            case "onMyTurn":
                List<Coord> selectableWorkers = (List<Coord>) objs.get(1);
                //TODO: ask the player to choose the worker for his turn
                break;

            case "onMyAction":
                List<Coord> movableSpaces = (List<Coord>) objs.get(1);
                Map<Level, List<Coord>> buildableSpaces = (Map<Level, List<Coord>>) objs.get(2);
                boolean canEndTurn = (boolean) objs.get(3);
                //TODO: ask the player to make an action
                break;

            case "onWin":
                String winner = (String)objs.get(1);
                outputStream.println("The winner is: " + winner);
                //TODO: end of the game
        }

    }

    private void handleGameMessage(){

    }

    private void onGodSelection(List<String> gods){

        boolean correct = false;

        while (!correct){
            outputStream.println("Choose a God, you can use \"[god_name] help\" to read the power of the God: ");
            for (String g : gods) {
                outputStream.println("- " + g);
            }

            String input = s.nextLine();

            for(String g : gods){
                if (input.toLowerCase().equals(g.toLowerCase())){
                    correct = true;
                    break;
                }
            }

            if (correct){
                List<Object> objects = new ArrayList<>();
                objects.add("onGodChosen");
                objects.add(input);
                serverConnection.asyncSend(objects);
            }
        }
    }

    public void run(){
        Executor exec = Executors.newFixedThreadPool(1);
        exec.execute(serverConnection);
        outputStream.println("You have entered the lobby.");
    }
}