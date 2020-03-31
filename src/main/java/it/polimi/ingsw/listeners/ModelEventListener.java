package it.polimi.ingsw.listeners;

public interface ModelEventListener extends Listener {
    public void onAllPlayersArrived();
    public void onBoardChanged();
    // public void onColorChosen();  I don't think this method is useful
    public void onGameReadyListener();
    public void onGodsChosenListener();
    public void onPlayerAddedListener(String nickname);
    public void onTurnChanged(String nick);
}