package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;

import java.util.Observable;

public class PlayerView{

    private Player player;
    private GameModel model;

    public PlayerView(){

    }

    public void setupView(Player p, GameModel m){
        this.player = p;
        this.model = m;
    }

    public Coord[] initializeWorkersPositions(){

    }

    public Worker chooseWorkerToMove(){

    }
}
