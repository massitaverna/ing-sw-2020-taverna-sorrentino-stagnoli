package it.polimi.ingsw.model;

import it.polimi.ingsw.model.handler.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final String nickname;
    private God god;
    private boolean hasWon;
    private List<Worker> workersList;
    private Color workerColor;
    private boolean isStartPlayer;

    public Player(String nickname){
        this.nickname = nickname;
        this.workerColor = null;
        this.workersList = new ArrayList<>();
        this.workersList.add(new Worker(this));
        this.workersList.add(new Worker(this));
        this.isStartPlayer = false;
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
        List<Worker> result = new ArrayList<>();
        for (Worker w : workersList) {
            result.add(w.clone());
        }
        return result;
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

    public boolean isStartPlayer() {
        return isStartPlayer;
    }

    void setAsStartPlayer() {
        isStartPlayer = true;
    }
    public void win(){
        hasWon = true;
    }
}
