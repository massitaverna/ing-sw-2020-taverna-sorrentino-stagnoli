package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;

import java.beans.PropertyChangeSupport;
import java.io.PrintStream;
import java.util.*;

public class View extends Observable {

    private BoardChangeListener BCL;
    private TurnListener TL;
    private Scanner s;
    private PrintStream outputStream;
    private PropertyChangeSupport mPcs =
            new PropertyChangeSupport(this);

    // stare in ascolto su questa variabile per controllare che sia stato creato il player
    private Player player;

    // bisogna stare in ascolto su questa variabile per ottenere l'input dell'utente
    private String choice;

    private GameModel model;

    public View(GameModel model){
        s = new Scanner(System.in);
        outputStream = new PrintStream(System.out);
        BCL = new BoardChangeListener();
        this.model = model;
        model.addPropertyChangeListener(BCL);
        TL = new TurnListener(this);
        model.addPropertyChangeListener(TL);
    }

    // chiamato dopo aver aperto una connessione
    public void createPlayer(){
        outputStream.println("What's your nickname? ");
        String nick = s.nextLine();
        String input;
        Color color;

        // se la queue è vuota
        if(model.getQueueState() == 0) {
            outputStream.println("Chose a color (RED, BLUE, YELLOW): ");
            input = s.nextLine().toUpperCase();
            color = Color.valueOf(input);
        }
        else {
            //TODO: controllare quali colori sono disponibili e valutare l'input
            String viableColors = model.getViableColorsToString();
            outputStream.println("Chose a color"+ viableColors +": ");
            input = s.nextLine().toUpperCase();
            color = Color.valueOf(input);
        }

        Player player = new Player(nick, color);
        mPcs.firePropertyChange("player", null, player);
    }

    public void setChoice(String choice){
        this.choice = choice;
        mPcs.firePropertyChange("choice", null, choice);
    }

    public String getNick(){
        return player.getNickname();
    }

}
