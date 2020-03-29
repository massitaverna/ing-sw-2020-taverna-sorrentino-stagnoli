package it.polimi.ingsw.view;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChallengerView {

    private GameModel model;
    private Scanner s;
    private PrintWriter outputStream;

    // Delego a una view generica i metodi comuni a tutti i player
    private View view;

    public ChallengerView(GameModel model){
        this.view = new View(model);
        this.s = new Scanner(System.in);
        this.outputStream = new PrintWriter(System.out);
    }

    public int chooseNumberOfPlayers(){
        outputStream.println("The challenger chooses the number of players: ");
        int n = s.nextInt();
        return n;
    }

    public String chooseStartingPlayer(){
        outputStream.println("The challenger chooses the starting player: ");

        List<String> players = this.model.requestPlayersNicknames();
        for(String name: players){
            outputStream.println(name);
        }

        String choose = s.next();
        while(!players.contains(choose)){
            choose = s.next();
        }

        return choose;
    }

    public List<String> chooseGods(){
        System.out.println("The challenger chooses the gods: ");

        int numPlayers = this.model.getQueueState();
        List<String> gods = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            gods.add(s.next());
        }

        return gods;
    }

    public void getNick (){
        view.getNick();
    }

    public void choseWorkerToMove(){
        view.choseWorkerToMove();
    }

    public void initializeWorkersPosition(){
        view.initializeWorkersPosition();
    }

}
