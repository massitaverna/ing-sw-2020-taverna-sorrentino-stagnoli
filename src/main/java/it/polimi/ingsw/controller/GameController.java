package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;


public class GameController {

    private PlayerChoseBuildListener PCBL;
    private PlayerChoseColorListener PCCL;
    private PlayerChoseMoveListener PCML;
    private PlayerChoseWorkerListener PCWL;

    private GameModel model;

    public GameController(){
        this.model = new GameModel();
    }
}
