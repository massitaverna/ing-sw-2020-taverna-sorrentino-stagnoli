package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.listeners.Model.*;
import it.polimi.ingsw.view.listeners.View.PlayerChoseBuildListener;
import it.polimi.ingsw.view.listeners.View.PlayerChoseMoveListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.PrintStream;
import java.util.*;

public class View implements AllPlayersArrivedListener, BoardChangeListener, ColorChosenListener, GameReadyListener,
                             GodsChosenListener, PlayerAddedListener, TurnChangedListener {

    private Scanner s;
    private PrintStream outputStream;


    // stare in ascolto su queste variabile per controllare che sia stato creato il player
    private String nickname;
    private PlayerChoseMoveListener PCMV;
    private PlayerChoseBuildListener PCBL;


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


    @Override
    public void onAllPlayersArrived() {

    }

    @Override
    public void onBoardChanged() {

    }

    @Override
    public void onColorChosen() {

    }

    @Override
    public void onGameReadyListener() {

    }

    @Override
    public void onGodsChosenListener() {

    }

    @Override
    public void onPlayerAddedListener() {

    }

    @Override
    public void onTurnChanged(String nickname) {
        if(this.nickname.equals(nickname)){
            outputStream.println("Where do you want to move?");
            String input = s.nextLine();
            PCMV.onPlayerChoseMove(input);
            outputStream.println("Where do you want to build?");
            input = s.nextLine();
            PCBL.onPlayerChoseBuild(input);
        }
    }
}