package it.polimi.ingsw.view;

import it.polimi.ingsw.exceptions.model.InvalidCoordinatesException;
import it.polimi.ingsw.listeners.PlayerViewEventListener;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.listeners.ModelEventListener;
import it.polimi.ingsw.listeners.ViewEventListener;

import java.io.PrintStream;
import java.util.*;

public class PlayerView implements ModelEventListener, EventSource {

    private Scanner s;
    private PrintStream outputStream;


    // stare in ascolto su queste variabile per controllare che sia stato creato il player
    private String nickname;
    private PlayerViewEventListener listener;


    private GameModel model;
    private String god;
    private boolean setupDone;

    public PlayerView(GameModel model){
        s = new Scanner(System.in);
        outputStream = new PrintStream(System.out);
        this.model = model;
        setupDone = false;
    }

    // chiamato dopo aver aperto una connessione
    public void createPlayer() {
        this.readNickname();
        this.getColor();
    }

    public void readNickname() {
        outputStream.println("Choose a nickname: ");
        String input = s.nextLine();

        while (!model.requestPlayersNicknames().contains(input)){
            outputStream.println("Nickname already taken, please choose another nickname: ");
            input = s.nextLine();
        }

        this.nickname = input;

        listener.onNicknameChosen(this, input);
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

        listener.onColorChosen(this, c);
    }

    // viene chiamata solo se entrambi i worker sono spostabili o sempre?
    public void chooseWorkerToMove(){
        outputStream.println("Choose a worker to move (1, 2): ");
        String input = s.nextLine();

        listener.onWorkerChosen(this, input);
    }

    public void initializeWorkersPosition(){
        outputStream.println("Where do you want to place the first worker?");
        while (true) {
            String input  = s.nextLine();
            Coord c = Coord.convertStringToCoord(input);
            try {
                Coord.validCoord(c);
                listener.onWorkerInitialized(this, c.x, c.y);
                break;
            }
            catch (InvalidCoordinatesException e) {
                outputStream.println("The format is incorrect.");
            }
        }

        /*outputStream.println("Where do you want to place the second worker?");
        input  = s.nextLine();
        listener.onSecondWorkerPositioned(input);*/
    }

    public void chooseGod(){
        outputStream.print("Choose a god from the list: ");
        // TODO: get the viable gods, print them and validate the input
        String input = s.nextLine();

        listener.onGodChosen(this, input);
    }

    public String getNickname() {
        return nickname;
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
    public void onGameReady() {
        outputStream.println("Set up is done, you are good to go!");

        model.getBoardView();
    }

    @Override
    public void onGodsChosen() {

    }

    @Override
    public void onPlayerAdded(String nickname) {
        outputStream.println(nickname + "has joined the game.");
        int currentNumber = model.getQueueState();
        int totalNumber = model.getNumPlayers();

        if (totalNumber != currentNumber)
            outputStream.println("Waiting for " + (totalNumber-currentNumber) + " more player/s");
    }

    @Override
    public void onMyTurn() {

    }

    // Should we divide this method based on the phase of the game (setup or play) o should I
    // have a bunch of if statements?
    @Override
    public void onTurnChanged(String nickname)  throws InvalidCoordinatesException{
        if (!setupDone) {
            if (god == null) {
                chooseGod();
            }
            else {
                initializeWorkersPosition();
            }
            return;
        }
        // if it's my turn to play with basic rules
        if(this.nickname.equals(nickname)){
            outputStream.println("Where do you want to move?");
            // the input should be like "A1"
            String input = s.nextLine();
            input = input.toUpperCase();
            Coord c = Coord.convertStringToCoord(input);
            while (!Coord.validCoord(c)){
                outputStream.println("Your input is invalid, please try again.");
                // the input should be like "A1"
                input = s.nextLine();
                input = input.toUpperCase();
                c = Coord.convertStringToCoord(input);
            }
            listener.onPlayerChoseMove(this, c);

            outputStream.println("Where do you want to build?");
            input = s.nextLine();
            c = Coord.convertStringToCoord(input);
            while (!Coord.validCoord(c)){
                outputStream.println("Your input is invalid, please try again.");
                // the input should be like "A1"
                input = s.nextLine();
                input = input.toUpperCase();
                c = Coord.convertStringToCoord(input);
            }
            listener.onPlayerChoseBuild(this, c);
        }
    }

    @Override
    public void addListener(Listener listener) {
        if (!(listener instanceof ViewEventListener)) {
            throw new IllegalArgumentException("Tried to register a non-ViewEventListener to View.");
        }
        this.listener = (ViewEventListener) listener;
    }
}