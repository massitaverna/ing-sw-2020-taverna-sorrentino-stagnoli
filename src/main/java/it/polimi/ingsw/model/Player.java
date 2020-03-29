package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Player {

    private String nickname;
    private God god;
    private boolean hasWon;
    /*private Collection<Rule> rules;*/
    private List<Worker> workerList;
    private Color workerColor;

    public Player(String nickname){
        this.nickname = nickname;
        this.workerColor = null;
        this.workerList = new ArrayList<Worker>();
        this.workerList.add(new Worker());
        this.workerList.add(new Worker());
        /*this.rules = new Collection<Rule>();*/
        this.hasWon = false;
    }

    public void setGod(God god){
        this.god = god;
    }

    public God getGod() {
        return god;
    }

    // prende come parametro il numero del lavoratore (1 o 2) e
    // restituisce il lavoratore corrispondente
    public Worker getWorker(int num){
        return workerList.get(num-1);
    }

    public String getNickname() {
        return nickname;
    }

    public void setWorkerColor(Color c){
        this.workerColor = c;
        this.workerList.get(0).setColor(this.workerColor);
        this.workerList.get(1).setColor(this.workerColor);
    }

    public Color getWorkerColor(){
        return this.workerColor;
    }

    public void win(){
        hasWon = true;
    }

    public void play(){

    }

    public void setUp(){

    }
}
