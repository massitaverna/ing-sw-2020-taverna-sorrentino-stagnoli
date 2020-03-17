package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Player {

    private String nickname;
    private God god;
    private boolean hasWon;
    private Collection<Rule> rules;
    private List<Worker> workerList;

    public Player(String nickname){
        this.nickname = nickname;
        workerList = new ArrayList<Worker>();
        workerList.add(new Worker());
        workerList.add(new Worker());
        rules = new Collection<Rule>();
        this.hasWon = false;
    }

    public void setGod(God god){
        this.god = god;
    }

    public void win(){

        // la logica per la vittoria va in questa classe?

        hasWon = true;
    }

    // prende come parametro il numero del lavoratore (1 o 2) e
    // restituisce il lavoratore corrispondente
    public Worker getWorker(int num){
        return workerList.get(num-1);
    }

    public God getGod() {
        return god;
    }

    public String getNickname() {
        return nickname;
    }
}
