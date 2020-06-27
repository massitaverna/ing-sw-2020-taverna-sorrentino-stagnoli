package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable, Cloneable {

    private static final long serialVersionUID = 6L;

    private final String nickname;
    private God god;
    //private boolean hasWon;
    private List<Worker> workersList;
    private Color workerColor;
    private boolean isStartPlayer;

    public Player(String nickname) {
        this.nickname = nickname;
        this.workerColor = null;
        this.workersList = new ArrayList<>();
        this.workersList.add(new Worker(this));
        this.workersList.add(new Worker(this));
        this.isStartPlayer = false;
        /*this.rules = new Collection<Rule>();*/
        //this.hasWon = false;
    }

    /**
     * Set the god for this player
     * @param god the god to set
     */
    public void setGod(God god){
        this.god = god;
    }

    /**
     * Get the god of this player
     * @return
     */
    public God getGod() {
        return god;
    }

    // prende come parametro il numero del lavoratore (1 o 2) e
    // restituisce il lavoratore corrispondente
    //utilizzato solo per inserirli nella board, in seguito verrano scelti
    //dalla board tramite le loro coordinate

    /**
     * Get a specific worker of this player
     * @param num the number of the worker to get (1 or 2)
     * @return a reference to the selected worker
     */
    public Worker getWorker(int num){
        return workersList.get(num);
    }

    /**
     * Get all the workers of this player
     * @return
     */
    public List<Worker> getWorkersList() {
        List<Worker> result = new ArrayList<>();
        for (Worker w : workersList) {
            result.add(w.clone());
        }
        return result;
    }

    /**
     * Get the nickname of this player
     * @return
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Set the color for the workers of this player
     * @param c
     */
    public void setWorkerColor(Color c){
        this.workerColor = c;
        this.workersList.get(0).setColor(this.workerColor);
        this.workersList.get(1).setColor(this.workerColor);
    }

    /**
     * Get the color of this player
     * @return
     */
    public Color getWorkerColor(){
        return this.workerColor;
    }

    /**
     * Check if this player is the starting one
     * @return
     */
    public boolean isStartPlayer() {
        return isStartPlayer;
    }

    /**
     * Set this player as the starting player
     */
    void setAsStartPlayer() {
        isStartPlayer = true;
    }

    /**
     * Clone
     * @return
     */
    @Override
    public Player clone() {
        Player result = new Player(this.nickname);
        result.god = this.god;
        result.workerColor = this.workerColor;
        result.workersList = new ArrayList<>();
        result.workersList.add(this.workersList.get(0).clone());
        result.workersList.add(this.workersList.get(1).clone());
        result.isStartPlayer = this.isStartPlayer;

        return result;
    }
}
