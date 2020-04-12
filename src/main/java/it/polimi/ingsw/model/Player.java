//mod
package it.polimi.ingsw.model;

import it.polimi.ingsw.model.god.God;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String nickname;
    private God god;
    private boolean hasWon;
    /*private Collection<Rule> rules;*/
    private List<Worker> workersList;
    private Color workerColor;

    public Player(String nickname){
        this.nickname = nickname;
        this.workerColor = null;
        this.workersList = new ArrayList<Worker>();
        this.workersList.add(new Worker(this));
        this.workersList.add(new Worker(this));
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
    //utilizzato solo per inserirli nella board, in seguito verrano scelti
    //dalla board tramite le loro coordinate
    public Worker getWorker(int num){
        return workersList.get(num-1);
    }

    public List<Worker> getWorkersList() {
        return workersList;
    }

    public String getNickname() {
        return nickname;
    }

    public void setWorkerColor(Color c){
        this.workerColor = c;
        this.workersList.get(0).setColor(this.workerColor);
        this.workersList.get(1).setColor(this.workerColor);
    }

    public Color getWorkerColor(){
        return this.workerColor;
    }

    public void win(){
        hasWon = true;
    }
}
