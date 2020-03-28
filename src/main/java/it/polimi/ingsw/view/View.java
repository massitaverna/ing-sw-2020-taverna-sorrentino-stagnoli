package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;

import java.io.PrintStream;
import java.util.*;

public class View extends Observable {

    private BoardChangeListener BCL;
    private TurnListener TL;
    private Scanner s;
    private PrintStream outputStream;
    private String nick;
    private String choice;
    private GameModel model;

    public View(GameModel model){
        s = new Scanner(System.in);
        outputStream = new PrintStream(System.out);
        BCL = new BoardChangeListener();
        this.model = model;
        model.addPropertyChangeListener(BCL);
    }

    public void parseNickname(){
        outputStream.println("What's your nickname? ");
        this.nick = s.nextLine();
        TL = new TurnListener(this);
        model.addPropertyChangeListener(TL);
    }

    public void setChoice(String choice){
        this.choice = choice;
    }

    public String getNick(){
        return this.nick;
    }

}
