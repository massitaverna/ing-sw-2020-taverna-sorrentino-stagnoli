package it.polimi.ingsw.listeners;

public interface ModelEventListener extends Listener {
    public void onAllPlayersArrived();
    public void onBoardChanged();
    public void onColorChosen();  //serve per far scegliere i colori secondo l'ordine di gioco in modo da evitare confilitti
    public void onGameReadyListener();
    public void onGodsChosenListener();
    public void onPlayerAddedListener(String nickname);
    public void onTurnChanged(String nick);
}
