package it.polimi.ingsw.view.listeners.Model;

public interface ModelEventListener {
    public void onAllPlayersArrived();
    public void onBoardChanged();
    public void onColorChosen();
    public void onGameReadyListener();
    public void onGodsChosenListener();
    public void onPlayerAddedListener();
    public void onTurnChanged();
}
