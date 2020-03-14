package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.Random;

public class Player {

    private String nickname;
    private IGod god;
    private Color color;
    private Worker[] workers;

    public Player(String name, IGod god, Color c){
        this.nickname=name;
        this.god = god;
        this.color = c;
        this.workers = new Worker[2];
        this.workers[0]=new Worker(this.color);
        this.workers[1]=new Worker(this.color);
    }

    //ritorna un riferimento alla lista dei workers
    public Worker[] getWorkers(){
        return workers;
    }

    //ritorna il nome del player
    public String getNickname(){
        return new String(this.nickname);
    }

    public IGod getGod(){
        return this.god;
    }

    public Color getColor(){
        return this.color;
    }

    //codice eseguito al momento della scelta delle posizioni iniziali.
    //per ora fa a random, poi chiamerà cose per avvisare il client
    public Coord[] ChooseStartingPositions(ArrayList<Coord> possiblePositions){
        Coord[] result = new Coord[2];

        result[0] = possiblePositions.get(new Random().nextInt(possiblePositions.size()));
        possiblePositions.remove(result[0]);
        result[1] = possiblePositions.get(new Random().nextInt(possiblePositions.size()));

        return result;
    }

    //codice eseguito al momento della scelta delle posizioni iniziali.
    //per ora fa a random, poi chiamerà cose per avvisare il client
    public Worker PickWorker(){
        return workers[new Random().nextInt(2)];
    }

    //codice eseguito al momento della scelta di movimento.
    //per ora fa a random, poi chiamerà cose per avvisare il client
    public Coord ChooseMove(ArrayList<Coord> possibleMoves){
        return possibleMoves.get(new Random().nextInt(possibleMoves.size()));
    }

    //codice eseguito al momento della scelta di costruzione.
    //per ora fa a random, poi chiamerà cose per avvisare il client
    public Coord ChooseBuild(ArrayList<Coord> possibleBuilds){
        return possibleBuilds.get(new Random().nextInt(possibleBuilds.size()));
    }

}
