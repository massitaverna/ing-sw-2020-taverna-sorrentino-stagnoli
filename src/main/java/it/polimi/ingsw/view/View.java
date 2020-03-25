package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

import java.io.PrintStream;
import java.util.*;

public class View extends Observable implements Runnable{

    private TurnChangerListener TCL;
    private Scanner s;
    private PrintStream outputStream;

    public View(){
        s = new Scanner(System.in);
        outputStream = new PrintStream(System.out);
    }

    private void showModel(GameModel model){
        ArrayList<Worker> workerList = model.getAllWorkers();
        outputStream.println("Board: ");
    }


    @Override
    public void run() {
        outputStream.println("What's your nickname? ");
        String nickname = s.next();
    }


}
