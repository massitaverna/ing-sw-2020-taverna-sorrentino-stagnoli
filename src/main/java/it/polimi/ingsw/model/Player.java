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
    private Color workerColor;

    public Player(String nickname, Color c){
        this.nickname = nickname;
        this.workerColor = c;
        this.workerList = new ArrayList<Worker>();
        this.workerList.add(new Worker(this.workerColor));
        this.workerList.add(new Worker(this.workerColor));
        this.rules = new Collection<Rule>();
        this.hasWon = false;
    }

    public void setGod(God god){
        this.god = god;
    }

    public void win(){
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

    public void play(){

    }

    public void setUp(){

    }
}
