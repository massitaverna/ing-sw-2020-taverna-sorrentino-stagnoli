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

    public Worker[] getWorkers(){
        return workers;
    }

    public String getNickname(){
        return new String(this.nickname);
    }

    public IGod getGod(){
        return this.god;
    }

    public Color getColor(){
        return this.color;
    }

    public Coord[] ChooseStartingPositions(ArrayList<Coord> possiblePositions){
        Coord[] result = new Coord[2];

        result[0] = possiblePositions.get(new Random().nextInt(possiblePositions.size()));
        possiblePositions.remove(result[0]);
        result[1] = possiblePositions.get(new Random().nextInt(possiblePositions.size()));

        return result;
    }

    public Worker PickWorker(){
        return workers[0];
    }

    public Coord ChooseMove(ArrayList<Coord> possibleMoves){
        return possibleMoves.get(new Random().nextInt(possibleMoves.size()));
    }

    public Coord ChooseBuild(ArrayList<Coord> possibleBuilds){
        return possibleBuilds.get(new Random().nextInt(possibleBuilds.size()));
    }

}
