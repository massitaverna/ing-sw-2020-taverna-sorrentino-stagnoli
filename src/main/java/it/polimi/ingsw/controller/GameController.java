package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.view.listeners.View.*;


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
