package it.polimi.ingsw.view;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModel2;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChallengerView {

    private GameModel2 model;

    public ChallengerView(GameModel2 model){
        this.model = model;
    }

    public int chooseNumberOfPlayers(){
        System.out.println("The challanger chooses the number of players: ");
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();

        return n;
    }

    public String chooseStartingPlayer(){
        System.out.println("The challanger chooses the starting player: ");

        List<String> players = this.model.requestPlayersNicknames();
        for(String name: players){
            System.out.println(name);
        }

        Scanner scan = new Scanner(System.in);
        String choose = scan.next();
        while(!players.contains(choose)){
            choose = scan.next();
        }

        return choose;
    }

    public List<String> chooseGods(){
        System.out.println("The challanger chooses the gods: ");

        int numPlayers = this.model.getNumPlayers();
        List<String> gods = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        for (int i = 0; i < numPlayers; i++) {
            gods.add(scan.next());
        }

        return gods;
    }

}
