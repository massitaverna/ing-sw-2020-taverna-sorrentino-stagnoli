package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.listeners.ModelEventListener;
import it.polimi.ingsw.listeners.ViewEventListener;
import it.polimi.ingsw.listeners.EventSource;
import it.polimi.ingsw.listeners.Listener;
import java.io.PrintStream;
import java.util.*;

public class View implements ModelEventListener, EventSource {

    private Scanner s;
    private PrintStream outputStream;


    // stare in ascolto su queste variabile per controllare che sia stato creato il player
    private String nickname;
    private ViewEventListener listener;


    private GameModel model;

    public View(GameModel model){
        s = new Scanner(System.in);
        outputStream = new PrintStream(System.out);
        this.model = model;
    }

    // chiamato dopo aver aperto una connessione
    public void createPlayer() {
        this.getNick();
        this.getColor();
    }

    public void getNick () {
        outputStream.println("Choose a nickname: ");
        String input = s.nextLine();

        while (!model.requestPlayersNicknames().contains(input)){
            outputStream.println("Nickname already taken, please choose another nickname: ");
            input = s.nextLine();
        }

        this.nickname = input;

        listener.onNicknameChosen(input);
    }

    public void getColor(){
        outputStream.println("Choose your color (RED, BLUE, YELLOW):");
        String input = s.nextLine();
        Color c = Color.valueOf(input);

        while (!model.getAvailableColors().contains(c)){
            outputStream.println("Color already taken, please choose a new one: ");
            input = s.nextLine();
            c = Color.valueOf(input);
        }

        listener.onColorChosen(c);
    }

    // viene chiamata solo se entrambi i worker sono spostabili o sempre?
    public void choseWorkerToMove(){
        outputStream.println("Choose a worker to move (1, 2): ");
        String input = s.nextLine();

        listener.onWorkerToMoveChosen(input);
    }

    public void initializeWorkersPosition(){
        outputStream.println("Where do you want to place the first worker?");
        String input  = s.nextLine();
        listener.onFirstWorkerPositioned(input);
        outputStream.println("Where do you want to place the second worker?");
        input  = s.nextLine();
        listener.onSecondWorkerPositioned(input);
    }

    public void choseGod(){
        outputStream.print("Chose a god between these: ");
        // TODO: get the viable gods, print them and validate the input
        String input = s.nextLine();

        listener.onGodChosen(input);
    }


    @Override
    public void onAllPlayersArrived() {
        outputStream.println("All player are connected. The game is about to start...");
    }

    @Override
    public void onBoardChanged() {
        model.getBoardView();
    }

//    @Override
//    public void onColorChosen() {
//        // Shouldn't the controller have this method? When a Player chooses his/her color
//        // the controller sees it and update the Player in the model
//    }

    @Override
    public void onGameReadyListener() {
        outputStream.println("Set up is done, you are good to go!");

        model.getBoardView();
    }

    @Override
    public void onGodsChosenListener() {

    }

    @Override
    public void onPlayerAddedListener(String nickname) {
        outputStream.println(nickname + "has joined the game.");
        int currentNumber = model.getQueueState();
        int totalNumber = model.getNumPlayers();

        if (totalNumber != currentNumber)
            outputStream.println("Waiting for " + (totalNumber-currentNumber) + " more player/s");
    }

    // Should we divide this method based on the phase of the game (setup or play) o should I
    // have a bunch of if statements?
    @Override
    public void onTurnChanged(String nickname) {
        // if it's my turn to play with basic rules
        if(this.nickname.equals(nickname)){
            outputStream.println("Where do you want to move?");
            // the input should be like "A1"
            String input = s.nextLine();
            input = input.toUpperCase();
            Coord c = convertStringToCoord(input);
            while (!Coord.validCoord(c)){
                outputStream.println("Your input is invalid, please try again.");
                // the input should be like "A1"
                input = s.nextLine();
                input = input.toUpperCase();
                c = convertStringToCoord(input);
            }
            listener.onPlayerChoseMove(c);

            outputStream.println("Where do you want to build?");
            input = s.nextLine();
            c = convertStringToCoord(input);
            while (!Coord.validCoord(c)){
                outputStream.println("Your input is invalid, please try again.");
                // the input should be like "A1"
                input = s.nextLine();
                input = input.toUpperCase();
                c = convertStringToCoord(input);
            }
            listener.onPlayerChoseBuild(c);
        }
    }

    private Coord convertStringToCoord(String input){
        int x = (int) input.charAt(0) - 65;
        int y = (int) input.charAt(1) - 1;

        return new Coord(x, y);
    }

    @Override
    public void addListener(Listener listener) {
        if (!(listener instanceof ViewEventListener)) {
            throw new IllegalArgumentException("Tried to register a non-ViewEventListener to View.");
        }
        this.listener = (ViewEventListener) listener;
    }
}