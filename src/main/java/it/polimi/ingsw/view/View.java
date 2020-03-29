package it.polimi.ingsw.view;

import com.sun.org.apache.xpath.internal.operations.Bool;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.listeners.Model.*;
import it.polimi.ingsw.listeners.View.ViewEventListener;
import java.io.PrintStream;
import java.util.*;

public class View implements ModelEventListener {

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

    }

    public void getNick () {
        outputStream.println("Chose a nickname: ");
        String input = s.nextLine();
        this.nickname = input;
        // TODO: definire un listener per la scelta del nick
        listener.onNicknameChosen(input);
    }

    // viene chiamata solo se entrambi i worker sono spostabili o sempre?
    public void choseWorkerToMove(){
        outputStream.println("Chose a worker to move (1, 2): ");
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
        // print the board
    }

    @Override
    public void onColorChosen() {
        outputStream.println("Chose your color (RED, BLUE, YELLOW):");
        String input = s.nextLine();
        Color[] viableColors = model.getViableColors();
        boolean f = false;

        while (!f){
            for (Color c: viableColors) {
                if(c.toString().equals(input)){
                    f = true;
                    break;
                }
            }

            if(!f){
                outputStream.println("Color already taken, choose a new one: ");
                input = s.nextLine();
            }
        }

        listener.onColorChosen(input);
    }

    @Override
    public void onGameReadyListener() {
        outputStream.println("Set up is done, you are good to go!");

        // print initial board
    }

    @Override
    public void onGodsChosenListener() {

    }

    @Override
    public void onPlayerAddedListener(String nickname) {

        /*TODO: print nickname + " has joined the game"
           get the number of connected player
           if it's not the number of total player
                print "Waiting for" + model.getNumPlayers-model.getQueueState + "more player"
           else
                do nothing*/

    }

    @Override
    public void onTurnChanged(String nickname) {
        if(this.nickname.equals(nickname)){
            outputStream.println("Where do you want to move?");
            String input = s.nextLine();
            listener.onPlayerChoseMove(input);
            outputStream.println("Where do you want to build?");
            input = s.nextLine();
            listener.onPlayerChoseBuild(input);
        }
    }
}