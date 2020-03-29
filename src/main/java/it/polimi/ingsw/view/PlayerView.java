package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;

import java.util.Observable;

public class PlayerView{

    private Player player;
    private GameModel2 model;

    public PlayerView(){

    }

    public void setupView(Player p, GameModel2 m){
        this.player = p;
        this.model = m;
    }

    public God initializeGod(){

    }

    public Coord[] initializeWorkersPositions(){

    }

    public Worker chooseWorkerToMove(){

    }

    public Coord chooseSpaceToMove(Worker w){

    }

    public Coord chooseSpaceToBuild(Worker w){

    }
}
